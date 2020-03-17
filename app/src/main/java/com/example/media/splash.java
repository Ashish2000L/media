package com.example.media;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.lang.Thread.sleep;

public class splash extends AppCompatActivity {

    TextView welcome,text,plswt;
    ProgressBar prog;
    FirebaseAuth mAuth;
    Thread reg_user,login,pub;
    FirebaseUser users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        text=(TextView)findViewById(R.id.user);
        plswt=(TextView)findViewById(R.id.plswt);
        prog=(ProgressBar)findViewById(R.id.prog);

        mAuth=FirebaseAuth.getInstance();
        users = mAuth.getCurrentUser();

        prog.setVisibility(View.VISIBLE);
        plswt.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!=null) {
            if (users.getDisplayName() != null) {
                text.setText(users.getDisplayName());
                reg_user=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(5000);
                            finish();
                            startActivity(new Intent(splash.this, Login.class));
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                });
                reg_user.start();
            }else {
                text.setText("User");
                pub = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sleep(5000);
                            finish();
                            startActivity(new Intent(splash.this, Login.class));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                pub.start();
            }
        }else {
            text.setText("User");
            //Toast.makeText(splash.this, "NO new user found", Toast.LENGTH_LONG).show();
            login=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sleep(5000);
                        finish();
                        startActivity(new Intent(splash.this, Login.class));
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            });
            login.start();
        }

    }
}
