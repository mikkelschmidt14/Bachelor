package com.example.schmi.bachelor;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET, passwordET;
    Button loginBtn, registerBtn;
    ConstraintLayout background;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        background = findViewById(R.id.background);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new login().execute();
            }
        });
        
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);

                registerIntent.putExtra("username", usernameET.getText().toString());

                Pair p1 = Pair.create(usernameET, ViewCompat.getTransitionName(usernameET));
                Pair p2 = Pair.create(registerBtn, ViewCompat.getTransitionName(registerBtn));
                Pair p3 = Pair.create(background, ViewCompat.getTransitionName(background));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation( LoginActivity.this, p1, p2, p3);

                startActivity(registerIntent, options.toBundle());
            }
        });
    }

    private class login extends AsyncTask<Void, Void, String> {

        Dialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this, "Attempting Login", "Please wait...");
        }

        @Override
        protected String doInBackground(Void... voids) {

            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();

            String data = Services.getAPI("user.php?username=" + username + "&password=" + password);

            return data;
        }



        @Override
        protected void onPostExecute(String data){
            dialog.dismiss();

            if(data.length() > 3) {
                Values.getInstance().setUsername(usernameET.getText().toString());
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

                //Pair p1 = Pair.create(addItemBtn, ViewCompat.getTransitionName(addItemBtn));
                Pair p1 = Pair.create(background, ViewCompat.getTransitionName(background));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this, p1);

                startActivity(mainIntent, options.toBundle());
            } else {
                Toast.makeText(LoginActivity.this, "Email or password was incorrect", Toast.LENGTH_SHORT).show();
            }

        }

    }

}
