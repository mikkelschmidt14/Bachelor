package com.example.schmi.bachelor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CreateItemActivity extends AppCompatActivity {

    private int GALLERY = 1, CAMERA = 2;

    Button addItem;
    EditText nameET, descriptionET;
    ImageView itemImage;
    Bitmap image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        //requestMultiplePermissions();

        nameET = findViewById(R.id.nameET);
        descriptionET = findViewById(R.id.descriptionET);
        addItem = findViewById(R.id.addItem);
        itemImage = findViewById(R.id.itemImage);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String s = Services.getStringImage(image);
                //imageView2.setImageBitmap(Services.StringToBitMap(s));
                if(validateInput()){
                    new createItem().execute();
                } else {
                    Toast.makeText(CreateItemActivity.this, "Input not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

    }

    private boolean validateInput(){

        String name = String.valueOf(nameET.getText());
        String description = String.valueOf(descriptionET.getText());

        return !(name.equals("") || description.equals("") || image==null);
    }

    private class createItem extends AsyncTask<Void, Void, Void>{

        Dialog dialog;
        int itemID = -1;

        @Override
        protected void onPreExecute(){
            dialog = ProgressDialog.show(CreateItemActivity.this, "Item is being created","Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String username = "mikkelschmidt14";

            String data = Services.getAPI("Item.php?username=" + username + "&itemname=" + String.valueOf(nameET.getText()));
            if(data.length() > 3){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CreateItemActivity.this, "Item already exists", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }

            Services.postAPI("Item.php?username=" + username + "&itemname=" + String.valueOf(nameET.getText()) + "&itemdescription=" + String.valueOf(descriptionET.getText()), image);

            data = Services.getAPI("Item.php?username=" + username + "&itemname=" + String.valueOf(nameET.getText()));
            System.out.println("data is");
            System.out.println(data);
            try {
                JSONArray jsons = new JSONArray(data);
                JSONObject json = jsons.getJSONObject(0);
                itemID = json.getInt("itemID");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid){
            dialog.dismiss();
            if(itemID != -1){

                Intent productIntent = new Intent(CreateItemActivity.this, ItemActivity.class);

                productIntent.putExtra("itemID", itemID);

                Pair p1 = Pair.create(nameET, ViewCompat.getTransitionName(nameET));
                Pair p2 = Pair.create(descriptionET, ViewCompat.getTransitionName(descriptionET));
                Pair p3 = Pair.create(itemImage, ViewCompat.getTransitionName(itemImage));
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CreateItemActivity.this, p1, p2, p3);

                CreateItemActivity.this.startActivity(productIntent, options.toBundle());

            }
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    image = bitmap;
                    //String path = saveImage(bitmap);
                    //Toast.makeText(CreateItemActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    itemImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateItemActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image = thumbnail;
            itemImage.setImageBitmap(thumbnail);
            //saveImage(thumbnail);
            //Toast.makeText(CreateItemActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

}
