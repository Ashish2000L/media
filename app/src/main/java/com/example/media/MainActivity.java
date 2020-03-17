package com.example.media;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import static android.graphics.Color.RED;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.YELLOW;
import static android.view.View.GONE;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button sign_out;
    ImageView profileImg;
    ProgressBar Horprog,prog_circular;
    FirebaseUser user;

    TextView tvprog,tvviews,username,userEmail,userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Welcome");

        mAuth=FirebaseAuth.getInstance();

        profileImg=(ImageView)findViewById(R.id.profileImg);
        prog_circular=(ProgressBar)findViewById(R.id.progress_circular);
        sign_out=(Button)findViewById(R.id.signout);
        Horprog=(ProgressBar)findViewById(R.id.Horprog);
        tvviews=(TextView)findViewById(R.id.tvviews);
        tvprog=(TextView)findViewById(R.id.tvprog);
        username=(TextView)findViewById(R.id.username);
        userEmail=(TextView)findViewById(R.id.userEmail);
        userUID=(TextView)findViewById(R.id.userUID);





        loadUserInfo();

      //  setSupportActionBar(toolbar);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                new singout().execute(100);
                sign_out.setVisibility(GONE);
            }
        });
    }

    private class singout extends AsyncTask<Integer,Integer,Integer> {

        @Override
        protected void onPreExecute() {
            sign_out.setVisibility(GONE);
            Horprog.setVisibility(View.VISIBLE);
            tvviews.setVisibility(View.VISIBLE);
            tvprog.setVisibility(View.VISIBLE);
            tvviews.setText("Logging you out, please wait!!");
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            int count=integers[0];
            int num=1;
            int i;
            for( i=0;i<count;i++){
                try {
                    sleep(500);
                    num=num*2;
                    i=num;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);

                if(isCancelled()){
                    break;
                }
            }
          return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tvprog.setText(""+values[0]+"%");
            Horprog.setProgress(values[0]);
            if (values[0] % 2 == 0) {

                tvviews.setTextColor(RED);
            }else {
                tvviews.setTextColor(BLUE);
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Horprog.setVisibility(GONE);
            tvprog.setVisibility(GONE);
            tvviews.setVisibility(GONE);
            finish();
            startActivity(new Intent(MainActivity.this,Login.class));
        }
    }



       private void loadUserInfo() {

        final FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                prog_circular.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .centerCrop()
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                prog_circular.setVisibility(GONE);
                                Log.e("TAG","Error loading Image",e);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Toast.makeText(MainActivity.this, "Image Loaded succesfully!!", Toast.LENGTH_SHORT).show();
                                prog_circular.setVisibility(GONE);
                                return false;
                            }
                        })
                        .into(profileImg);

            }

            if (user.getDisplayName() != null) {

                username.setText(user.getDisplayName());
            }else{
                username.setVisibility(GONE);
            }

            if(user.getEmail()!=null){
                userEmail.setText(user.getEmail());
            }else{
                userEmail.setVisibility(GONE);
            }

            if(user.getUid()!=null){
                userUID.setText(user.getUid());
            }else{
                userUID.setVisibility(GONE);
            }
        }

    }







   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menus,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){           //this is used when more then one items are to be listed in it
            case R.id.meu:

                FirebaseAuth.getInstance().signOut();
                new singout().execute(100);
                break;
        }

        return true;
    }*/
}
