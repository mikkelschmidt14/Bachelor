package com.example.schmi.bachelor;

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

import com.example.schmi.bachelor.R;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameET, nameET, passwordET, repasswordET;
    Button registerBtn;
    ConstraintLayout background;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().getEnterTransition().excludeTarget(android.R.id.statusBarBackground, true);

        background = findViewById(R.id.background);
        usernameET = findViewById(R.id.usernameET);
        nameET = findViewById(R.id.nameET);
        passwordET = findViewById(R.id.passwordET);
        repasswordET = findViewById(R.id.repasswordET);
        registerBtn = findViewById(R.id.registerBtn);
        
        
        String username = getIntent().getExtras().getString("username");
        usernameET.setText(username);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    new register().execute();
                } else {
                    Toast.makeText(RegisterActivity.this, "Input not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    private boolean validateInput(){

        String username = usernameET.getText().toString();
        String name = nameET.getText().toString();
        String password = passwordET.getText().toString();
        String repassword = repasswordET.getText().toString();

        return (password.equals(repassword) && password.length() >= 4 && username.length() >= 4 && name.length() > 0);
    }

    private class register extends AsyncTask<Void, Void, String> {

        Dialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(RegisterActivity.this, "Attempting to register user", "Please wait...");
        }

        @Override
        protected String doInBackground(Void... voids) {

            String username = usernameET.getText().toString();
            String name = nameET.getText().toString();
            String password = passwordET.getText().toString();

            String data = Services.getAPI("user.php?username=" + username);
            if(data.length() > 3)
                return "User already exists";

            Services.postAPI("user.php?username=" + username + "&password=" + password + "&name=" + name);
            return "success";
        }



        @Override
        protected void onPostExecute(String msg){
            dialog.dismiss();

            if(msg.equals("success")) {
                Values.getInstance().setUsername(usernameET.getText().toString());
                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);

                Pair p1 = Pair.create(background, ViewCompat.getTransitionName(background));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(RegisterActivity.this, p1);

                startActivity(mainIntent, options.toBundle());
            } else {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }

        }

    }

}
