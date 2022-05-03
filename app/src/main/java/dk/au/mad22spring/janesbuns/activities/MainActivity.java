package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad22spring.janesbuns.R;

public class MainActivity extends AppCompatActivity {

    Button butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        butt = findViewById(R.id.button);

        butt.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        startActivity(new Intent(this, AddBunActivity.class));
    }
}