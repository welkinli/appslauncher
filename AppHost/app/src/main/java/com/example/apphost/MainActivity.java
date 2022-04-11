package com.example.apphost;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MainActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    View mBtInstall,mBtStart;
    public static final String TARGETAPP="com.example.targetapp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtInstall = findViewById(R.id.btInstall);
        mBtStart = findViewById(R.id.btStart);
        mProgressBar = findViewById(R.id.pgDownload);
        mBtInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apkName = "target-app.apk";
                String filePath = getFilesDir().getAbsolutePath() + "/" + apkName;
                copyAssetFile(apkName, filePath);
                mProgressBar.setProgress(100);
                installApk(filePath);
            }
        });
        mBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTargetActivity();
            }
        });
        TextView openMarket=findViewById(R.id.openMarket);
        openMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMarket();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean hasInstall=checkInstallSucess(TARGETAPP);
        Log.d(TAG, "onResume: install sucess?:"+hasInstall);
        if(hasInstall){
            mBtStart.setVisibility(View.VISIBLE);
            mBtInstall.setVisibility(View.INVISIBLE);
        }else{
            mBtStart.setVisibility(View.INVISIBLE);
            mBtInstall.setVisibility(View.VISIBLE);
        }
    }

    public void installApk(String apkPath) {
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(this, "com.example.apphost.fileProvider", file);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);

    }

    public void startTargetActivity() {
        //Uri contentUri = FileProvider.getUriForFile(this, "com.example.apphost.fileProvider", file);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(TARGETAPP, "com.example.targetapp.MainActivity");
        //intent.setDataAndType(contentUri,"*/*");
        //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setComponent(cn);
        startActivity(intent);
    }

    public boolean checkInstallSucess(String packageName) {
        if(TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }  catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void copyAssetFile(String sourceName, String targetPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = this.getAssets().open(sourceName);
            File target = new File(targetPath);
            if (target.exists())
                return;
            out = new FileOutputStream(target);

            byte[] buffer = new byte[4096];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            Log.w(TAG + ":copy", "error occur while copy", e);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openMarket(){
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse("market://details?id=com.xunmeng.pinduoduo"));
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(it);
    }
}