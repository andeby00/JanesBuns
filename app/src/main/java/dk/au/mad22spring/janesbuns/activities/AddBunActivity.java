package dk.au.mad22spring.janesbuns.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

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
    ActivityResultLauncher<String> launcher;


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

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            imgUri = result;
            if (imgUri != null) image.setImageURI(imgUri);
        });
    }

    private void selectImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        launcher.launch("image/*");
    }

    private void addBun() {
        StorageReference storageReference = storageRef.child("CreamBuns/" + UUID.randomUUID() + ".jpg");

        storageReference.putFile(imgUri)
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "onSuccess: " + task.getMetadata().getPath());
                    CreamBun creamBun = new CreamBun(
                            name.getText().toString(),
                            Integer.parseInt(amount.getText().toString()),
                            Double.parseDouble(price.getText().toString()),
                            task.getMetadata().getPath()
                    );

                    db.collection("creamBuns").add(creamBun);

                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(exception -> {
                    Log.d(TAG, "onFailure: image");

                    setResult(RESULT_CANCELED);
                    finish();
                });

    }
}
