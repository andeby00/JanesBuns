package dk.au.mad22spring.janesbuns.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad22spring.janesbuns.OrderDetailsAdapter;
import dk.au.mad22spring.janesbuns.OrderDetailsAdminAdapter;
import dk.au.mad22spring.janesbuns.OrderViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.fragments.TopbarFragment;
import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;

public class AdminOrderDetailsActivity extends AppCompatActivity {

    RecyclerView rcvOrderDetailsAdmin;
    int i;
    OrderViewModel vm;
    OrderDetailsAdminAdapter orderDetailsAdminAdapter;
    TextView textViewStatus, textViewDate, textViewTime, textViewAddress, textViewName;
    FirebaseFirestore db;
    Button btnConfirmed, btnCancelled, btnSent, btnReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        i = intent.getIntExtra("index", 0);

        vm = new ViewModelProvider(this).get(OrderViewModel.class);
        vm.initializeVM(this, true);

        textViewStatus = findViewById(R.id.txtOrderDetailsAdminStatus);
        textViewDate = findViewById(R.id.txtOrderDetailsAdminDate);
        textViewTime = findViewById(R.id.txtOrderDetailsAdminTime);
        textViewAddress = findViewById(R.id.txtOrderDetailsAdminAddress);
        textViewName = findViewById(R.id.txtOrderDetailsAdminName);
        btnConfirmed = findViewById(R.id.btnOrderDetailsAdminConfirmed);
        btnCancelled = findViewById(R.id.btnOrderDetailsAdminCancelled);
        btnReceived = findViewById(R.id.btnOrderDetailsAdminRecieved);
        btnSent = findViewById(R.id.btnOrderDetailsAdminSent);

        orderDetailsAdminAdapter = new OrderDetailsAdminAdapter(i);
        rcvOrderDetailsAdmin = findViewById(R.id.rcvOrderDetailsAdminOrders);
        rcvOrderDetailsAdmin.setLayoutManager(new LinearLayoutManager(this));
        rcvOrderDetailsAdmin.setAdapter(orderDetailsAdminAdapter);
        orderDetailsAdminAdapter.updateOrderList(vm.getOrders().getValue());

        vm.getOrders().observe(this, orders -> {
            orderDetailsAdminAdapter.updateOrderList(orders);

            db.collection("users").document(orders.get(i).userUid).get().addOnSuccessListener(documentSnapshot -> {
                User tempUser = documentSnapshot.toObject(User.class);
                textViewAddress.setText(tempUser.address.toString());
                textViewName.setText(tempUser.fullName.toString());
            });

            textViewStatus.setText(orders.get(i).status.toString());
            textViewDate.setText(orders.get(i).date.toString());
            textViewTime.setText(orders.get(i).time.toString());
        });

        btnConfirmed.setOnClickListener(this::onClickConfirm);
        btnSent.setOnClickListener(this::onClickSent);
        btnCancelled.setOnClickListener(this::onClickCancelled);
        btnReceived.setOnClickListener(this::onClickReceived);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fmcOrderDetailsAdminTopbar, TopbarFragment.newInstance("BACK"))
                .commitNow();
    }

    private void onClickCancelled(View view) {
        String userId;
        userId = vm.getOrders().getValue().get(i).userUid;
        Order tempOrder = vm.getOrders().getValue().get(i);
        tempOrder.setStatus(Order.Status.CANCELLED);
        db.collection("users").document(userId).update("order", tempOrder);
    }

    private void onClickReceived(View view) {
        String userId;
        userId = vm.getOrders().getValue().get(i).userUid;
        Order tempOrder = vm.getOrders().getValue().get(i);
        tempOrder.setStatus(Order.Status.RECEIVED);
        db.collection("users").document(userId).update("order", tempOrder);
    }

    private void onClickSent(View view) {
        String userId;
        userId = vm.getOrders().getValue().get(i).userUid;
        Order tempOrder = vm.getOrders().getValue().get(i);
        tempOrder.setStatus(Order.Status.SENT);
        db.collection("users").document(userId).update("order", tempOrder);
    }

    private void onClickConfirm(View view) {
        String userId;
        userId = vm.getOrders().getValue().get(i).userUid;
        Order tempOrder = vm.getOrders().getValue().get(i);
        tempOrder.setStatus(Order.Status.CONFIRMED);
        db.collection("users").document(userId).update("order", tempOrder);
    }
}