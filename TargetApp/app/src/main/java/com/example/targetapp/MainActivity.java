package com.example.targetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    Button mNotify;
    public static int mNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotify=findViewById(R.id.btNotify);
        mNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.apphost.MY_BROADCAST");
                intent.putExtra("message","定位app定位成功，手Q收到消息:+"+(++mNum));
                intent.setComponent(new ComponentName("com.example.apphost","com.example.apphost.MessageReceiver"));
                sendBroadcast(intent);
                Log.d("welkinli", "onClick: send broadcast ok ");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void checkReceiveFile(Intent it){
        Uri uri=it.getData();
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            //InputStream in = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            OutputStream out  = new FileOutputStream(getFilesDir()+"/tt.apk");
            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}