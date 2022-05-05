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
    FirebaseFirestore db;

    ProfileViewModel vm;

    private Button buttonLogout, buttonAddBun;
    private TextView textViewFullName, textViewEmail, textViewPhone, textViewAddress, textViewPostalCode, textViewCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        vm.initializeVM();

        buttonLogout = findViewById(R.id.logoutButtonProfile);
        buttonAddBun = findViewById(R.id.addBunButtonProfile);
        textViewFullName = findViewById(R.id.fullNameTextViewProfile);
        textViewEmail = findViewById(R.id.emailTextViewProfile);
        textViewPhone = findViewById(R.id.phoneTextViewProfile);
        textViewAddress = findViewById(R.id.addressTextViewProfile);
        textViewPostalCode = findViewById(R.id.postalCodeTextViewProfile);
        textViewCity = findViewById(R.id.cityTextViewProfile);

        setDetails();

        /*User currentUser = vm.getCurrentUser();

        if(currentUser != null) {
            if(currentUser.isAdmin) {
                buttonAddBun.setVisibility(View.VISIBLE);

                buttonAddBun.setOnClickListener(this::onClickAddBun);
            }
            else buttonAddBun.setVisibility(View.GONE);
        }*/

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
                textViewPostalCode.setText(String.valueOf(postalCode));
                textViewCity.setText(city);
            }
        });
    }

    private void onClickAddBun(View view) {
        startActivity(new Intent(this, AddBunActivity.class));
    }

    private void onClickLogout(View view) {
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}