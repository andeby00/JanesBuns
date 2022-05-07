package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Optional;

import dk.au.mad22spring.janesbuns.LoginViewModel;
import dk.au.mad22spring.janesbuns.ProfileViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.User;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ProfileViewModel vm;

    private Button buttonLogout;
    private TextView textViewFullName, textViewEmail, textViewPhone, textViewAddress, textViewPostalCode, textViewCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        vm.initializeVM(this);

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
        vm.getCurrentUser().observe(this, user -> {
            textViewFullName.setText(user.fullName);
            textViewEmail.setText(user.email);
            textViewPhone.setText(user.phone);
            textViewAddress.setText(user.address);
            textViewPostalCode.setText(String.valueOf(user.postalCode));
            textViewCity.setText(user.city);
        });
    }

    private void onClickLogout(View view) {
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}