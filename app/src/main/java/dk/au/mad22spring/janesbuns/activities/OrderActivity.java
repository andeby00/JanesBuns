package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import dk.au.mad22spring.janesbuns.OrderAdapter;
import dk.au.mad22spring.janesbuns.OrderViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.fragments.TopbarFragment;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.IOrderItemClickedListener {

    RecyclerView rcvOrder;
    OrderViewModel vm;
    OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        vm = new ViewModelProvider(this).get(OrderViewModel.class);
        vm.initializeVM(this);

        orderAdapter = new OrderAdapter(this, this);
        rcvOrder = findViewById(R.id.rcvOrderOrders);
        rcvOrder.setLayoutManager(new LinearLayoutManager(this));
        rcvOrder.setAdapter(orderAdapter);
        orderAdapter.updateOrderList(vm.getOrders().getValue());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rcvOrderOrders, TopbarFragment.newInstance("BACK"))
                .commitNow();
    }

    @Override
    public void onOrderClicked(int index) {
        if(vm.getCurrentUser().getValue() != null) {
            if(vm.getCurrentUser().getValue().isAdmin) {
                startActivity(new Intent(this, AdminOrderDetailsActivity.class));
            } else startActivity(new Intent(this, OrderDetailsActivity.class));
        }
    }
}