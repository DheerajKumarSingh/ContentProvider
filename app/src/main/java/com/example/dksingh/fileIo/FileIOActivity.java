package com.example.dksingh.fileIo;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dksingh.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileIOActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG=FileIOActivity.class.getSimpleName();

    private static final String FILE_NAME="sample.txt";

    private Context mContext;

    private Button buttonWriteToFile,buttonReadFromFile;
    private TextView textViewContentFromFile;
    private EditText editTextUserMessage;


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
        String string=editTextUserMessage.getText().toString();
        if(isStringEmpty(string)){
            try{
                writeToFile(FILE_NAME,string,MODE_PRIVATE);
            }catch (Exception exception){
                Toast.makeText(mContext,getString(R.string.error_generic),Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean isStringEmpty(String string){
        if(string!=null && !string.equals("") && string.length()>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populatTextFromPreviousSession();
    }

    private void populateTheReadText(){
        try{
            textViewContentFromFile.setText(readFromFile(FILE_NAME));
            textViewContentFromFile.setVisibility(View.VISIBLE);
        }catch (Exception exception){
            Toast.makeText(mContext,getString(R.string.error_generic),Toast.LENGTH_SHORT).show();
            textViewContentFromFile.setVisibility(View.GONE);
        }
    }

    private void populatTextFromPreviousSession(){
        String readContent=null;
        try{
            readContent= readFromFile(FILE_NAME);
            textViewContentFromFile.setVisibility(View.VISIBLE);
        }catch (Exception exception){
            Toast.makeText(mContext,getString(R.string.error_generic),Toast.LENGTH_SHORT).show();
            textViewContentFromFile.setVisibility(View.GONE);
        }
        textViewContentFromFile.setText("From Previous Session: \n "+readContent);
    }

    private String readFromFile(String fileName) throws FileNotFoundException, IOException{
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

    private void writeToFile(String fileName,String sourceText,int MODE) throws
            FileNotFoundException,IOException{

        FileOutputStream fileOutputStream=openFileOutput(fileName,MODE);
        OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(sourceText);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }
}
