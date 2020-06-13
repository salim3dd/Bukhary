package com.qays3dd.buhk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Splash_Activity extends AppCompatActivity {
    Boolean go;
    DatabaseHelper db = new DatabaseHelper(this);
    GlobalVar v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (!database.exists()) {
            db.getReadableDatabase();
            if (copyDatabase(this)) {
                go = true;
                Toast.makeText(Splash_Activity.this, "تم نسخ قاعدة البيانات بنجاح", Toast.LENGTH_SHORT).show();
            } else {
                go = false;
                 Toast.makeText(Splash_Activity.this, "خطأ لم يتم نسخ قاعدة البيانات", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            go=true;
        }

        v = (GlobalVar) getApplicationContext();
        ImageView imageView = findViewById(R.id.imageViewSplash_logo);
        Animation animS = AnimationUtils.loadAnimation(this, R.anim.scale_splashscreen_anim);
        imageView.setAnimation(animS);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (go) {
                    startActivity(new Intent(Splash_Activity.this, MainActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
