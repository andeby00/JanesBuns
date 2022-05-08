package dk.au.mad22spring.janesbuns.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Optional;

import dk.au.mad22spring.janesbuns.LoginViewModel;
import dk.au.mad22spring.janesbuns.ProfileViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.User;
import dk.au.mad22spring.janesbuns.utils.GlobalVariables;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseAuth mAuth;

    ProfileViewModel vm;

    Button buttonLogout;
    TextView textViewFullName, textViewEmail, textViewPhone, textViewAddress, textViewPostalCode, textViewCity, textViewOrders;
    Spinner spinner;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        vm = new ViewModelProvider(this).get(ProfileViewModel.class);
        vm.initializeVM(this);

        buttonLogout = findViewById(R.id.logoutButtonProfile);
        textViewOrders = findViewById(R.id.txtProfileViewOrder);
        textViewFullName = findViewById(R.id.fullNameTextViewProfile);
        textViewEmail = findViewById(R.id.emailTextViewProfile);
        textViewPhone = findViewById(R.id.phoneTextViewProfile);
        textViewAddress = findViewById(R.id.addressTextViewProfile);
        textViewPostalCode = findViewById(R.id.postalCodeTextViewProfile);
        textViewCity = findViewById(R.id.cityTextViewProfile);

        spinner = findViewById(R.id.spnProfileSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vm.getCurrencyCodes().getValue());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);
        vm.getCurrencyCodes().observe(this, currencyCodes -> {
            dataAdapter.addAll(currencyCodes);
            dataAdapter.notifyDataSetChanged();
        });

        setDetails();

        buttonLogout.setOnClickListener(this::onClickLogout);
        textViewOrders.setOnClickListener(this::onClickOrder);

        sharedPreferences = getSharedPreferences(GlobalVariables.sharedPrefsKey,Context.MODE_PRIVATE);
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

    private void onClickOrder(View view) {
        Intent i = new Intent(this, OrderActivity.class);
        i.putExtra("isAdmin", vm.getCurrentUser().getValue().isAdmin);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String currency = parent.getItemAtPosition(position).toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(GlobalVariables.currency, currency);

        editor.commit();

        Toast.makeText(parent.getContext(), "Selected: " + currency, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}