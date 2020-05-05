package com.example.media;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.params.RggbChannelVector;
import android.icu.text.RelativeDateTimeFormatter;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static java.lang.Thread.sleep;

public class profile extends AppCompatActivity {

    TextView tvVerification,displayname;
    private static final int CHOOSE_IMAGE =101 ;
    private FirebaseAuth mAuth;
    ImageView imageView;
    EditText name;
    Button upload;
    Uri uriprofileimage;
    Thread toMain;
    ProgressBar progress,progress_bar;
    String  profileImageUrl;
    FirebaseUser user;
    StorageReference mStoarageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");
        mAuth = FirebaseAuth.getInstance();
        mStoarageRef= FirebaseStorage.getInstance().getReference();

        displayname=(TextView)findViewById(R.id.tvdispname);
        imageView=(ImageView)findViewById(R.id.image);
        name=(EditText)findViewById(R.id.edit_txt);
        upload=(Button)findViewById(R.id.btnUpload);
        progress=(ProgressBar)findViewById(R.id.progress_bar);
        tvVerification=(TextView)findViewById(R.id.tvVerified);
        progress_bar=(ProgressBar)findViewById(R.id.progress_Bar);

       // loadUserInfo();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImageChooser();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress_bar.setVisibility(View.VISIBLE);
                SaveUserInfo();
            }


        });

        toMain=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    progress_bar.setVisibility(View.VISIBLE);
                    sleep(2000);
                    finish();
                    startActivity(new Intent(profile.this,MainActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(profile.this, Login.class));
        }
    }

    private void SaveUserInfo() {
        String Displayname=name.getText().toString();
        displayname.setText(Displayname);
        if(Displayname.isEmpty()){

            name.setError("Please Enter your Name");
            name.requestFocus();
            return;
        }

        FirebaseUser User=mAuth.getCurrentUser();       //by this way we can access info of user and write it back

        if(User!=null){
            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder()        //getting users profile image url
                    .setDisplayName(Displayname)                                  //
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            User.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(profile.this, "Profile Name updated", Toast.LENGTH_SHORT).show();

                            name.setVisibility(View.GONE);
                            displayname.setVisibility(View.VISIBLE);
                            upload.setVisibility(View.GONE);
                            toMain.start();
                        }
                    });
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE    &&  resultCode==RESULT_OK  &&  data!=null  && data.getData()!=null){
            uriprofileimage=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriprofileimage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void uploadImageToFirebaseStorage() {

        final StorageReference profileImageRef= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriprofileimage!=null){
            progress.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriprofileimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progress.setVisibility(View.GONE);
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();

                                    //tvVerification.setText(profileImageUrl);
                                    Toast.makeText(profile.this, "Profile Image Uploaded", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }}).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progress.setVisibility(View.GONE);
                            Toast.makeText(profile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void showImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select profile Image"),CHOOSE_IMAGE);
    }


}
