package dk.au.mad22spring.janesbuns.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import dk.au.mad22spring.janesbuns.CartAdapter;
import dk.au.mad22spring.janesbuns.CartViewModel;
import dk.au.mad22spring.janesbuns.CreamBunAdapter;
import dk.au.mad22spring.janesbuns.MainViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;

public class CartActivity extends AppCompatActivity implements CartAdapter.ICreamBunItemClickedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    RecyclerView rcvCart;
    TextView textPrice;
    EditText edtDate, edtTime;
    CartAdapter cartAdapter;
    Button buyButton;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    CartViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        vm = new ViewModelProvider(this).get(CartViewModel.class);
        vm.initializeVM(this);

        cartAdapter = new CartAdapter(this);
        rcvCart = findViewById(R.id.rcvCartOrder);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(cartAdapter);
        cartAdapter.updateCreamBunList(vm.getCart().getValue());

        textPrice = findViewById(R.id.txtCartPrice);
        edtDate = findViewById(R.id.edtCartDate);
        edtTime = findViewById(R.id.edtCartTime);
        buyButton = findViewById(R.id.btnCartBuy);

        buyButton.setOnClickListener(view -> buyCart());
        buyButton.setEnabled(false);

        edtDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONDAY),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        edtTime.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(
                    this,
                    this,
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    true).show();
        });

        vm.getCart().observe(this, creamBuns -> {
            cartAdapter.updateCreamBunList(creamBuns);
            Double tempTotal = 0.0;
            for (CreamBun creamBun: creamBuns) {
                tempTotal += creamBun.price;
            }
            textPrice.setText(tempTotal.toString() + R.string.quantity);
        });
    }

    private void buyCart() {
        User tempUser = vm.getCurrentUser().getValue();
        String uid = mAuth.getCurrentUser().getUid();

        Order order = new Order(
                vm.getCart().getValue(),
                edtDate.getText().toString(),
                edtTime.getText().toString(),
                Order.Status.RECEIVED,
                uid
        );

        tempUser.orders.add(new Order(
                vm.getCart().getValue(),
                edtDate.getText().toString(),
                edtTime.getText().toString(),
                Order.Status.RECEIVED
        ));

        db.collection("orders").add(order).addOnSuccessListener(task -> {
            db.collection("users").document(uid).update("orders", tempUser.orders).addOnSuccessListener(task1 -> {
                Toast.makeText(this, "Order received", Toast.LENGTH_LONG).show();
                vm.clearCart();
                finish();
            }).addOnFailureListener(task1 -> {
                Toast.makeText(this, "Order failed", Toast.LENGTH_LONG).show();
            });
        }).addOnFailureListener(task1 -> {
            Toast.makeText(this, "Order failed", Toast.LENGTH_LONG).show();
        });

    }

    @Override
    public void onCreamBunClicked(int index) {
        vm.removeFromCart(index);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        edtDate.setText(day + "/" + month + "-" + year);
        if(!edtTime.getText().toString().equals(""))
            buyButton.setEnabled(true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        edtTime.setText(hour + ":" + minute);
        if(!edtDate.getText().toString().equals(""))
            buyButton.setEnabled(true);
    }
}