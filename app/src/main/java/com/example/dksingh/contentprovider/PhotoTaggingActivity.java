package com.example.dksingh.contentprovider;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class PhotoTaggingActivity extends BaseActivity {

    private Button buttonTagPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phototagging);

        buttonTagPhoto=(Button)findViewById(R.id.buttonTagPhoto);

        buttonTagPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkWhetherAllPermissionsPresentForPhotoTagging()){
                    Toast.makeText(PhotoTaggingActivity.this,"Go ahead, you have all permissions",Toast.LENGTH_LONG).show();
                }else{
                    requestRunTimePermissions(PhotoTaggingActivity.this, permissionsNeededForPhotoTagging, MY_PHOTO_TAGGING_PERMISSIONS);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PHOTO_TAGGING_PERMISSIONS) {
            if (checkWhetherAllPermissionsPresentForPhotoTagging()) {
                Toast.makeText(this, "Please go ahead do your stuff", Toast.LENGTH_SHORT).show();
            } else {
                String permissionString=getDeniedPermissionsAmongPhototaggingPermissions().length==1?"Permission":"Permissions";
                Snackbar.make(findViewById(android.R.id.content), permissionString+" denied, photo tagging will not work. To enable now click here",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(PhotoTaggingActivity.this, getDeniedPermissionsAmongPhototaggingPermissions(), MY_PHOTO_TAGGING_PERMISSIONS);
                    }
                }).show();
            }
        }
    }
}
