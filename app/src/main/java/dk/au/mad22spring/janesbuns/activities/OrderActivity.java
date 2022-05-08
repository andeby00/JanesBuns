package dk.au.mad22spring.janesbuns.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import dk.au.mad22spring.janesbuns.OrderAdapter;
import dk.au.mad22spring.janesbuns.OrderViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.fragments.TopbarFragment;
import dk.au.mad22spring.janesbuns.models.User;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.IOrderItemClickedListener {

    RecyclerView rcvOrder;
    OrderViewModel vm;
    OrderAdapter orderAdapter;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent i = getIntent();
        isAdmin = i.getBooleanExtra("isAdmin", false);

        vm = new ViewModelProvider(this).get(OrderViewModel.class);
        vm.initializeVM(this, isAdmin);

        orderAdapter = new OrderAdapter(this, this);
        rcvOrder = findViewById(R.id.rcvOrderOrders);
        rcvOrder.setLayoutManager(new LinearLayoutManager(this));
        rcvOrder.setAdapter(orderAdapter);
        orderAdapter.updateOrderList(vm.getOrders().getValue());
        orderAdapter.updateUserOrderList(vm.getCurrentUser().getValue());

        vm.getCurrentUser().observe(this, user -> orderAdapter.updateUserOrderList(user));
        if(isAdmin) {
            vm.getOrders().observe(this, orders -> orderAdapter.updateOrderList(orders));
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fmcOrderTopbar, TopbarFragment.newInstance("BACK"))
                .commitNow();
    }

    @Override
    public void onOrderClicked(int index) {
        if(vm.getCurrentUser().getValue() != null) {
            if(vm.getCurrentUser().getValue().isAdmin) {
                Intent i = new Intent(this, AdminOrderDetailsActivity.class);
                i.putExtra("index", index);
                startActivity(i);
            } else {
                Intent i = new Intent(this, OrderDetailsActivity.class);
                i.putExtra("index", index);
                startActivity(i);
            }
        }
    }
}