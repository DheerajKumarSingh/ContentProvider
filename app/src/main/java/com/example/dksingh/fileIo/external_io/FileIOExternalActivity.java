package com.example.dksingh.fileIo.external_io;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dksingh.R;
import com.example.dksingh.contentprovider.BaseActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileIOExternalActivity extends BaseActivity implements View.OnClickListener{


    private static final String TAG=FileIOExternalActivity.class.getSimpleName();
    private static final String FILE_NAME="sample.txt";
    private Context mContext;
    private Button buttonWriteToFile,buttonReadFromFile;
    private TextView textViewContentFromFile;
    private EditText editTextUserMessage;
    private UserAction recentUserAction;
    enum UserAction{
        READ,WRITE
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileio_activity);

        mContext=getApplicationContext();

        buttonReadFromFile=(Button)findViewById(R.id.buttonWriteToFile);
        buttonWriteToFile=(Button)findViewById(R.id.buttonReadFromFile);
        textViewContentFromFile=(TextView)findViewById(R.id.textViewContentFromFile);
        editTextUserMessage=(EditText)findViewById(R.id.editTextUserMessage);

        buttonReadFromFile.setOnClickListener(this);
        buttonWriteToFile.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonWriteToFile: writeContentToFile(); break;
            case R.id.buttonReadFromFile: populateTheReadText();break;
            default: break;
        }
    }


    private void writeContentToFile(){
        recentUserAction=UserAction.WRITE;
        String string=editTextUserMessage.getText().toString();
        if(isStringEmpty(string)){
            try{
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    writeToExternalStorageFile(FILE_NAME,string);
                } else {
                    requestRunTimePermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION);
                }

            }catch (FileNotFoundException exception){
                Toast.makeText(mContext,getString(R.string.error_string_file_not_found),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populatTextFromPreviousSession();
    }

    private void populateTheReadText()  {
        recentUserAction=UserAction.READ;
        try{
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                textViewContentFromFile.setText(readTextFromExternalStorage(FILE_NAME));
                textViewContentFromFile.setVisibility(View.VISIBLE);
            } else {
                requestRunTimePermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION);
            }

        }catch (FileNotFoundException exception){
            Toast.makeText(mContext,getString(R.string.error_string_file_not_found),Toast.LENGTH_SHORT).show();
            textViewContentFromFile.setVisibility(View.GONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populatTextFromPreviousSession(){
        recentUserAction=UserAction.READ;
        String readContent=null;
        try{

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                readContent= readTextFromExternalStorage(FILE_NAME);
            } else {
                requestRunTimePermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},EXTERNAL_STORAGE_PERMISSION);
            }


            textViewContentFromFile.setVisibility(View.VISIBLE);
        }catch (Exception exception){
            Toast.makeText(mContext,getString(R.string.error_generic),Toast.LENGTH_SHORT).show();
            textViewContentFromFile.setVisibility(View.GONE);
        }
        textViewContentFromFile.setText("From Previous Session: \n "+readContent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length == 1) {
            if (requestCode == EXTERNAL_STORAGE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (recentUserAction == UserAction.WRITE) {
                    writeContentToFile();
                } else if (recentUserAction == UserAction.READ) {
                    populateTheReadText();
                }
            }

        }
    }




    /**
     * This method reads the text content for internal memory
     * @param fileName
     * @return String represented the read text
     * @throws FileNotFoundException
     * @throws IOException
     */
     String readFromFile(String fileName) throws FileNotFoundException, IOException {
        String readString="";

        FileInputStream fileInputStream=openFileInput(fileName);
        InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder=new StringBuilder(readString);
        while ((readString=bufferedReader.readLine())!=null){
            stringBuilder.append(readString);
        }
        inputStreamReader.close();
        return stringBuilder.toString();
    }

    /**
     * This file writes the text to an internal file
     * @param fileName - name of the file
     * @param sourceText - The text that needs to be written to the file
     * @param MODE
     * @throws FileNotFoundException
     * @throws IOException
     */
     void writeToFile(String fileName,String sourceText,int MODE) throws
            FileNotFoundException,IOException{

        FileOutputStream fileOutputStream=openFileOutput(fileName,MODE);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(sourceText);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

    /**
     * Overloaded writeToFile which will be used by writeToExternalStorage method
     * @param file
     * @param sourceText
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
     File writeToFile(File file, String sourceText) throws
            FileNotFoundException, IOException{
        FileWriter fileWriter=new FileWriter(file);
        fileWriter.write(sourceText);
        fileWriter.flush();
        fileWriter.close();
        return file;
    }

    /**
     * A utilitarian method that returns true if String is not empty
     * @param string
     * @return
     */
     boolean isStringEmpty(String string){
        if(string!=null && !string.equals("") && string.length()>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Writes a text to file on external storage
     * @param filename
     * @param text
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected File externalStorageDirectory;
    protected void writeToExternalStorageFile(String filename,String text) throws
            FileNotFoundException, IOException {
        if(isExternalStorageWritable()){
            externalStorageDirectory=new File(Environment.getExternalStorageDirectory(),getPackageName());
            externalStorageDirectory.mkdir();
            writeToFile(new File(externalStorageDirectory,filename),text);
        }
    }

    /**
     *  Reads the text from external Storage
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    protected String readTextFromExternalStorage(String fileName) throws
            FileNotFoundException, IOException{
        String readString="";
        if(isExternalStorageReadable()){
            if(externalStorageDirectory==null){
                externalStorageDirectory=new File(Environment.getExternalStorageDirectory(),getPackageName());
            }
            readString=readFromFile(new File(externalStorageDirectory.getAbsolutePath()+"/"+fileName));
        }
        return readString;
    }

    /**
     * Utlitarin method that takes File as an argument and returns the textual content of file
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private String readFromFile(File file) throws FileNotFoundException, IOException{
        String readString="";
        BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder=new StringBuilder(readString);
        while ((readString=bufferedReader.readLine())!=null){
            stringBuilder.append(readString);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
