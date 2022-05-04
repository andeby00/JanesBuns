package dk.au.mad22spring.janesbuns.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.UserModel;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //private DatabaseReference reference;
    FirebaseFirestore db;

    private Button buttonLogout;
    private TextView textViewFullName, textViewEmail, textViewPhone, textViewAddress, textViewPostalCode, textViewCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        //reference = FirebaseDatabase.getInstance("https://janes--buns-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users");
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
            UserModel currentUser = documentSnapshot.toObject(UserModel.class);
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
        /*
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userProfile = snapshot.getValue(UserModel.class);

                if(userProfile != null) {
                    String fullName = userProfile.fullName;
                    String email = userProfile.email;
                    String phone = userProfile.phone;

                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewPhone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void onClickLogout(View view) {
        mAuth.signOut();
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }
}