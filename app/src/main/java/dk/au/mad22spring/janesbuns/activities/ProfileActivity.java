package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.User;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    private Button buttonLogout;
    private TextView textViewFullName, textViewEmail, textViewPhone, textViewAddress, textViewPostalCode, textViewCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        buttonLogout = findViewById(R.id.logoutButtonProfile);
        textViewFullName = findViewById(R.id.fullNameTextViewProfile);
        textViewEmail = findViewById(R.id.emailTextViewProfile);
        textViewPhone = findViewById(R.id.phoneTextViewProfile);
        textViewAddress = findViewById(R.id.addressTextViewProfile);
        textViewPostalCode = findViewById(R.id.postalCodeTextViewProfile);
        textViewCity = findViewById(R.id.cityTextViewProfile);

        setDetails();

        buttonLogout.setOnClickListener(this::onClickLogout);

    }

    private void setDetails() {
        String userId = mAuth.getCurrentUser().getUid();
        Log.d("profile", userId);

        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            User currentUser = documentSnapshot.toObject(User.class);
            if(currentUser != null) {
                String fullName = currentUser.email;
                String email = currentUser.email;
                String phone = currentUser.phone;
                String address = currentUser.address;
                int postalCode = currentUser.postalCode;
                String city = currentUser.city;

                textViewFullName.setText(fullName);
                textViewEmail.setText(email);
                textViewPhone.setText(phone);
                textViewAddress.setText(address);
                textViewPostalCode.setText(postalCode);
                textViewCity.setText(city);
            }
        });
    }

    private void onClickLogout(View view) {
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}