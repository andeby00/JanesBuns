package dk.au.mad22spring.janesbuns.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.CreamBun;

public class AddBunActivity extends AppCompatActivity {
    private static final String TAG = "AddBunActivity";

    FirebaseFirestore db;
    EditText name, amount, price;
    Button butt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bun);

        name = findViewById(R.id.editTextTextPersonName);
        amount = findViewById(R.id.editTextTextPersonName3);
        price = findViewById(R.id.editTextTextPersonName2);
        butt2 = findViewById(R.id.button2);

        butt2.setOnClickListener(view -> addBun());

        db = FirebaseFirestore.getInstance();
    }

    private void addBun() {
        CreamBun creamBun = new CreamBun(
                name.getText().toString(),
                Integer.parseInt(amount.getText().toString()),
                Double.parseDouble(price.getText().toString())
        );

        db.collection("creamBuns").add(creamBun);
    }
}