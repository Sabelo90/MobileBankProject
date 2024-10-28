package com.molefe.bankufs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.molefe.bankufs.Database.ConnectHelper;

import java.sql.Connection;
import java.sql.SQLException;

public class SplashActivity extends AppCompatActivity {

    Connection connection;
    Animation topAnim,bottomAnim;
    ImageView image;
    TextView logo , slogan;
    private static final int SPLASH_DELAY_MS = 6000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animination);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.botton_animination);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animination);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.botton_animination);
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById((R.id.txtSql));
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

         ConnectHelper c = new ConnectHelper(this);
        connection = c.getConnection();
        if (c!= null) {
            Toast.makeText(this, "Connection Succeeded", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    Intent intent = new Intent(SplashActivity.this, LogIn.class);
                    startActivity(intent);
                    try {
                            connection.close();
                    }catch (SQLException e){

                    }
                    finish();

                }
            }, SPLASH_DELAY_MS);


        }










    }
}