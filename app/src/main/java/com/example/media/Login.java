package com.example.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.view.View.VISIBLE;
import static java.lang.Thread.sleep;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    SubmitButton login;
    TextView not_regestered,textView;
    EditText email,password;
    ProgressBar progressBar,HprogressBar;
    FirebaseUser users;
    Intent intent;
    Thread main,main_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        textView=(TextView)findViewById(R.id.tvView);
        not_regestered=(TextView)findViewById(R.id.notreg);
        login=(SubmitButton)findViewById(R.id.btnlogin);
        email=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.pswd);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        HprogressBar=(ProgressBar)findViewById(R.id.Hprogress);

        not_regestered.setOnClickListener(this);
        login.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        users= mAuth.getCurrentUser();
    }

    private void userlogin(){
        String Email=email.getText().toString().trim();
        String Password=password.getText().toString().trim();

        if(Email.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            email.setError("Please Enter a Valid Email");
            email.requestFocus();
            return;
        }
        if (Password.isEmpty()){
            password.setError("Password is Required");
            password.requestFocus();
            return;
        }
        if(Password.length()<6){
            password.setError("Minimum length should be 6");
            password.requestFocus();
            return;
        }


        progressBar.setVisibility(VISIBLE);
        textView.setVisibility(VISIBLE);
        textView.setTextColor(BLACK);
        textView.setText("Please wait....");
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                HprogressBar.setVisibility(VISIBLE);
                if(task.isSuccessful()){
                   // textView.setText("Almost Done...");
                   // textView.setTextColor(GREEN);
                    if(users.isEmailVerified()&&(users.getDisplayName()!=null || users.getPhotoUrl()!=null)) {
                        HprogressBar.setVisibility(View.GONE);
                        textView.setText("Logging you in, Please wait....");
                        textView.setTextColor(GREEN);
                        finish();
                        startActivity(new Intent(Login.this,MainActivity.class));

                    }else
                        if (users.isEmailVerified() && (users.getDisplayName()==null || users.getPhotoUrl() == null)) {
                            textView.setText("Logging you in, Please wait....");
                            textView.setTextColor(GREEN);
                            finish();
                            intent=new Intent(Login.this,profile.class);
                            startActivity(intent);
                    }else
                        if (!users.isEmailVerified()) {
                            progressBar.setVisibility(View.GONE);
                            textView.setText("Please Verify your Email for login!!");
                            textView.setTextColor(RED);
                        }

                }else{
                    progressBar.setVisibility(View.GONE);
                    textView.setText("Login Unsuccessful!!");
                    textView.setTextColor(RED);

                }
            }
        });


  /*      main_activity=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(2000);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        main=new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    HprogressBar.setVisibility(VISIBLE);
                    sleep(2000);
                    finish();
                    intent=new Intent(Login.this,profile.class);
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    @Override
    protected void onStart() {      //This is just to check the user is loggeg in or not
        super.onStart();

        if(mAuth.getCurrentUser()!=null ){
            if(users.isEmailVerified()&&(users.getDisplayName()!=null || users.getPhotoUrl()!=null)) {
                finish();
                startActivity(new Intent(Login.this,MainActivity.class));
            }
            else if(users.isEmailVerified() && (users.getDisplayName().isEmpty() || users.getPhotoUrl()==null)) {
              finish();
              startActivity(new Intent(Login.this,profile.class));
            }
            else if(!users.isEmailVerified()){
                textView.setText("Please Verify your Email for login!!");
                textView.setTextColor(RED);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.notreg:
                startActivity(new Intent(Login.this,Signup.class));
                break;
            case R.id.btnlogin:
                userlogin();
                break;
        }
    }
}
