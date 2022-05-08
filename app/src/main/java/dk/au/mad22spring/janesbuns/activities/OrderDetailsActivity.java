package dk.au.mad22spring.janesbuns.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import dk.au.mad22spring.janesbuns.OrderDetailsAdapter;
import dk.au.mad22spring.janesbuns.OrderViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.fragments.TopbarFragment;

public class OrderDetailsActivity extends AppCompatActivity {

    RecyclerView rcvOrderDetails;
    int index;
    OrderViewModel vm;
    OrderDetailsAdapter orderDetailsAdapter;
    TextView textViewStatus, textViewDate, textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent i = getIntent();
        index = i.getIntExtra("index", 0);

        vm = new ViewModelProvider(this).get(OrderViewModel.class);
        vm.initializeVM(this, false);

        textViewStatus = findViewById(R.id.txtOrderDetailsStatus);
        textViewDate = findViewById(R.id.txtOrderDetailsDate);
        textViewTime = findViewById(R.id.txtOrderDetailsTime);

        orderDetailsAdapter = new OrderDetailsAdapter(this, index);
        rcvOrderDetails = findViewById(R.id.rcvOrderDetailsOrders);
        rcvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        rcvOrderDetails.setAdapter(orderDetailsAdapter);
        orderDetailsAdapter.updateUserOrderList(vm.getCurrentUser().getValue());
        vm.getCurrentUser().observe(this, user -> {
            orderDetailsAdapter.updateUserOrderList(user);
            textViewStatus.setText(user.orders.get(index).status.toString());
            textViewDate.setText(user.orders.get(index).date.toString());
            textViewTime.setText(user.orders.get(index).time.toString());
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fmcOrderDetailsTopbar, TopbarFragment.newInstance("BACK"))
                .commitNow();

    }
}