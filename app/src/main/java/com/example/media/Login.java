package com.example.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
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
    Integer integer;
    Boolean verification;
    Thread main,main_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        textView = (TextView) findViewById(R.id.tvView);
        not_regestered = (TextView) findViewById(R.id.notreg);
        login = (SubmitButton) findViewById(R.id.btnlogin);
        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.pswd);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        HprogressBar = (ProgressBar) findViewById(R.id.Hprogress);

        not_regestered.setOnClickListener(this);
        login.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        users = mAuth.getCurrentUser();
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

       // new login().execute(Email,Password);  ///this is for the execution of asynvtask

        progressBar.setVisibility(VISIBLE);
        textView.setVisibility(VISIBLE);
        textView.setTextColor(BLACK);
        textView.setText("Please wait....");
<<<<<<< HEAD
=======
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
>>>>>>> parent of 97c439d... signout option added


            Integer count=0;
            if(Email!=null && Password!=null) {
                textView.setText("Email is not null");
                do { count++;
                    if(count>1){
                        textView.setText("Login Unsuccesful, working on it Again....");
                        textView.setTextColor(BLUE);

                        if(count==3){
                            progressBar.setVisibility(View.GONE);
                            HprogressBar.setVisibility(View.GONE);
                            textView.setText("Can't login now, Try again later");
                            textView.setTextColor(RED);
                            return;
                        }
                    }
                    mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                           if (mAuth.getCurrentUser()!= null) {
                                textView.setText("user not null");
                                if (mAuth.getCurrentUser().isEmailVerified()) {
                                    textView.setText("Verified");
                                    verification = true;
                                } else {
                                    textView.setText("Not verified");
                                    verification = false;
                                }
                                if (mAuth.getCurrentUser() != null) {

                                    textView.setText("user not null");
                                    if (verification && (users.getDisplayName() != null || users.getPhotoUrl() != null)) {
                                      //This is to send user to main activity
                                        textView.setText("User Email verified");

                                            finish();
                                            startActivity(new Intent(Login.this, MainActivity.class));


                                    } else if (verification && (users.getDisplayName() == null || users.getPhotoUrl() == null)) {
                                        //This is to send user to upload profile activity
                                        textView.setText("profile not updated");

                                            finish();
                                            startActivity(new Intent(Login.this, profile.class));


                                    } else if (!verification) {
                                         //This is to tell user that he has not verified his mail
                                        HprogressBar.setVisibility(View.GONE);
                                        progressBar.setVisibility(View.GONE);
                                        textView.setText("Please Verify your Email for login!!");
                                        textView.setTextColor(RED);
                                    }
                                } else {
                                    //User has not found
                                    HprogressBar.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);
                                    textView.setText("An error Occur, Please login again!!");
                                    textView.setTextColor(RED);
                                }
                           }
                        }else {
                                //User Unsuccessful in login
                                HprogressBar.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                                textView.setText("Login Unsuccessful!!");
                                textView.setTextColor(RED);
                            }
                        }

                    });
                } while(users!=null);
            }
    }


/*    private class login extends AsyncTask<String,Integer,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            HprogressBar.setVisibility(VISIBLE);
            progressBar.setVisibility(VISIBLE);
            textView.setVisibility(VISIBLE);
            textView.setTextColor(BLACK);
            textView.setText("Please wait....");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String emails=strings[0];
            String pass=strings[1];
            Integer count=0;
           publishProgress(null,1);
            if(emails!=null && pass!=null) {
                publishProgress(null,2);
               do { count++;
                    if(count>1){
                        publishProgress(0);
                    }
                    mAuth.signInWithEmailAndPassword(emails, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            publishProgress(null,3);
                            if (task.isSuccessful()) {
                                publishProgress(null,4);
                                if (mAuth.getCurrentUser() != null) {
                                    publishProgress(null,5);
                                    if (users.isEmailVerified() && (users.getDisplayName() != null || users.getPhotoUrl() != null)) {
                                        publishProgress(1,6);//This is to send user to main activity
                                        try {
                                            sleep(3000);
                                            finish();
                                            startActivity(new Intent(Login.this,MainActivity.class));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    } else if (users.isEmailVerified() && (users.getDisplayName() == null || users.getPhotoUrl() == null)) {
                                        publishProgress(2,6);//This is to send user to upload profile activity
                                        try {
                                            sleep(3000);
                                            finish();
                                            startActivity(new Intent(Login.this,profile.class));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    } else if (!users.isEmailVerified()) {
                                        publishProgress(3,6); //This is to tell user that he has not verified his mail
                                    }
                                } else {
                                    publishProgress(4,7); //User has not found
                                }
                            } else {
                                publishProgress(5,8); //User Unsuccessful in login
                            }
                        }
                    });
                } while(users!=null);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            HprogressBar.setProgress(values[1]);
            switch (values[0]){
                case 0:
                    textView.setText("Login Unsuccesful, working on it Again....");
                    textView.setTextColor(GREEN);

                    break;

                case 1:
                    HprogressBar.setVisibility(View.GONE);
                    textView.setText("Logging you in, Please wait....");
                    textView.setTextColor(GREEN);
                    try {
                        sleep(1000);
                        finish();
                        startActivity(new Intent(Login.this,MainActivity.class));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    break;

                case 2:
                    HprogressBar.setVisibility(View.GONE);
                    textView.setText("Logging you in, Please wait....");
                    textView.setTextColor(GREEN);
                    try {
                        sleep(1000);
                        finish();
                        startActivity(new Intent(Login.this,profile.class));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    break;

                case 3:
                    HprogressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    textView.setText("Please Verify your Email for login!!");
                    textView.setTextColor(RED);

                    break;

                case 4:
                    HprogressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    textView.setText("An error Occur, Please login again!!");
                    textView.setTextColor(RED);

                    break;

                case 5:
                    HprogressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    textView.setText("Login Unsuccessful!!");
                    textView.setTextColor(RED);
            }

          /*  switch (values[1]){
                case 0:
                    Toast.makeText(Login.this, "logging in Again!!", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(Login.this, "Loading Data!!", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(Login.this, "Email and Password is Not null!!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Login.this, "Singing you in!!", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(Login.this, "succesfully singed in!!", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(Login.this, "User exist!!", Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(Login.this, "verifying email,photo,name", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(Login.this, "User not found!!", Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(Login.this, "Unnable to login!!", Toast.LENGTH_SHORT).show();
            }*/
       /* }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/

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
