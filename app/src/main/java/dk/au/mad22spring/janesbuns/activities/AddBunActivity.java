package dk.au.mad22spring.janesbuns.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.CreamBun;

public class AddBunActivity extends AppCompatActivity {
    private static final String TAG = "AddBunActivity";

    FirebaseFirestore db;
    StorageReference storageRef;

    EditText name, amount, price;
    Button butt2;
    ImageView image;
    Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bun);


        name = findViewById(R.id.editTextTextPersonName);
        amount = findViewById(R.id.editTextTextPersonName3);
        price = findViewById(R.id.editTextTextPersonName2);
        butt2 = findViewById(R.id.button2);
        image = findViewById(R.id.imgAddBunImage);

        butt2.setOnClickListener(view -> addBun());
        image.setOnClickListener(view -> selectImage());

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    private void selectImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == 200) {
                // Get the url of the image from data
                imgUri = data.getData();
                if (null != imgUri) {
                    // update the preview image in the layout
                    image.setImageURI(imgUri);
                }
            }
        }
    }

    private void addBun() {

        StorageReference storageReference = storageRef.child("CreamBuns/test3.jpg");

        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata().getPath());
                        CreamBun creamBun = new CreamBun(
                                name.getText().toString(),
                                Integer.parseInt(amount.getText().toString()),
                                Double.parseDouble(price.getText().toString()),
                                taskSnapshot.getMetadata().getPath()
                        );

                        db.collection("creamBuns").add(creamBun);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d(TAG, "onFailure: image");
                    }
                });

        finish();
    }
}