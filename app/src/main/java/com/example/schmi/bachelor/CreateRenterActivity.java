package com.example.schmi.bachelor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateRenterActivity extends AppCompatActivity {

    Button create;
    EditText renterNameET, emailET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_renter);

        create = findViewById(R.id.create);
        renterNameET = findViewById(R.id.renterNameET);
        emailET = findViewById(R.id.emailET);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()){
                    customSelectDeviceDialog dialog = new customSelectDeviceDialog(CreateRenterActivity.this, String.valueOf(renterNameET.getText()), String.valueOf(emailET.getText()));
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "input is not valid",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private boolean validateInput(){

        String renterName = String.valueOf(renterNameET.getText());
        String email = String.valueOf(emailET.getText());

        return !(renterName.equals("") || email.equals("") || !(email.contains("@") && email.contains(".")));
    }

}
