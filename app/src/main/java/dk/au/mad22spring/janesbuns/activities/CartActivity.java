package dk.au.mad22spring.janesbuns.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import dk.au.mad22spring.janesbuns.CartAdapter;
import dk.au.mad22spring.janesbuns.CartViewModel;
import dk.au.mad22spring.janesbuns.CreamBunAdapter;
import dk.au.mad22spring.janesbuns.MainViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.models.CreamBun;

public class CartActivity extends AppCompatActivity implements CartAdapter.ICreamBunItemClickedListener {
    RecyclerView rcvCart;
    TextView textPrice;
    CartAdapter cartAdapter;

    CartViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        vm = new ViewModelProvider(this).get(CartViewModel.class);
        vm.initializeVM(this);

        cartAdapter = new CartAdapter(this);
        rcvCart = findViewById(R.id.rcvCartOrder);
        rcvCart.setLayoutManager(new LinearLayoutManager(this));
        rcvCart.setAdapter(cartAdapter);
        cartAdapter.updateCreamBunList(vm.getCart().getValue());

        textPrice = findViewById(R.id.txtCartPrice);

        vm.getCart().observe(this, creamBuns -> {
            cartAdapter.updateCreamBunList(creamBuns);
            Double tempTotal = 0.0;
            for (CreamBun creamBun: creamBuns) {
                tempTotal += creamBun.price;
            }
            textPrice.setText(tempTotal.toString() + R.string.quantity);
        });
    }

    @Override
    public void onCreamBunClicked(int index) {
        vm.removeFromCart(index);
    }
}