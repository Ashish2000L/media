package com.example.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.icu.lang.UCharacterEnums;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.spark.submitbutton.SubmitButton;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.Thread.sleep;

public class Signup extends AppCompatActivity implements View.OnClickListener {

    private EditText name,email,username,password,cnfpassword;
   Button reg;
   private ProgressBar progress;
   private TextView text,login;

    private FirebaseAuth mAuth;
    Thread Singup_thread;
    Intent intent;
    FirebaseUser users;
    String Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setTitle("Signup");
                                                                //All the commmented things are just piece od shit just ignore them and this is my frustration

        login=(TextView)findViewById(R.id.tvlogin);
        reg=(Button) findViewById(R.id.btnreg);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        username=(EditText)findViewById(R.id.usernm);
        cnfpassword=(EditText)findViewById(R.id.cnfpass);
        progress=(ProgressBar)findViewById(R.id.progressbar);
        text=(TextView)findViewById(R.id.tvuser);

        reg.setOnClickListener(this);
        login.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance(); //Initialization of firebase Auth

    }
    private void registerUser(){
        String Name=name.getText().toString();
        String Email=email.getText().toString().trim();
        String Username=username.getText().toString().trim();
        String Password=password.getText().toString().trim();
        String Confirm_Password=cnfpassword.getText().toString().trim();

        if(Name.isEmpty()){
            name.setError("Name is Required");
            name.requestFocus();
            return;
        }
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
        if (Username.isEmpty()){
            username.setError("Username is Required");
            username.requestFocus();
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
        if (Confirm_Password.isEmpty()){
            cnfpassword.setError("Confirm Password is Required");
            cnfpassword.requestFocus();
            return;
        }
        if(!Confirm_Password.matches(Password)){
            cnfpassword.setError("Passwords not matched");
            password.requestFocus();
            return;
        }


        progress.setVisibility(VISIBLE);
        text.setVisibility(VISIBLE);
        text.setText("Please wait....");
        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                text.setText("Registring user....");
                if(task.isSuccessful()){
                    progress.setVisibility(GONE);
                    text.setTextColor(GREEN);
                   text.setText("User Regestation Successfull!!");
                   progress.setVisibility(VISIBLE);
                            text.setText("Sending please wait...");
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                    progress.setVisibility(GONE);
                                    text.setText("Please Verify and proceed to Login!!");
                                    text.setTextColor(GREEN);
                                    email.setText("");
                                    password.setText("");
                                    cnfpassword.setText("");
                                    username.setText("");
                                    name.setText("");
                                    }else {
                                      text.setText("Unable to send mail Try again");
                                    }
                                }
                            });

                }else {
                    if(task.getException()instanceof FirebaseAuthUserCollisionException){
                        progress.setVisibility(GONE);
                        text.setTextColor(BLUE);
                        text.setText("User already registered, Proceed to Login  ");
                        login.setVisibility(VISIBLE);
                    }else {
                        progress.setVisibility(GONE);
                        text.setTextColor(RED);
                        text.setText("Regesrtion Unsuccessful ");
                    }
                }
            }
        });



     /*   Singup_thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    progress.setVisibility(VISIBLE);
                    sleep(2000);
                    text.setText("Click to Verify");
                    text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            users.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progress.setVisibility(GONE);
                                    Toast.makeText(Signup.this, "Verification mail sent successfully!!", Toast.LENGTH_LONG).show();
                                    text.setText("Verify and proceed to Login!!");
                                    text.setTextColor(GREEN);
                                }
                            });

                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
*/
       /* pause=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sleep(2000);
                    return;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
  /*  private void singin(){
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    text.setText("Verifying...");
                    text.setTextColor(GREEN);
                   EmailVerification();

                }
            }
        });
    }

*/
 /*   private void EmailVerification() {
        users.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progress.setVisibility(GONE);
                Toast.makeText(Signup.this, "Verification mail sent successfully!!", Toast.LENGTH_LONG).show();
                text.setText("Verify and proceed to Login!!");
                text.setTextColor(GREEN);
                logout();
            }
        });
    }

    private void logout(){

        FirebaseAuth.getInstance().signOut();
        text.setText("your are succesfully singged out!!");
    }

*/


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnreg:
                registerUser();
                break;
            case R.id.tvlogin:
                startActivity(new Intent(Signup.this,Login.class));
                break;
        }

    }
}
