package dk.au.mad22spring.janesbuns.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.Volley;

import dk.au.mad22spring.janesbuns.CreamBunAdapter;
import dk.au.mad22spring.janesbuns.MainViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.fragments.CartFragment;
import dk.au.mad22spring.janesbuns.fragments.TopbarFragment;
import dk.au.mad22spring.janesbuns.models.User;
import dk.au.mad22spring.janesbuns.utils.OrderService;

public class MainActivity extends AppCompatActivity implements CreamBunAdapter.ICreamBunItemClickedListener {
    private static final String TAG = "MainActivity";
    
    RecyclerView rcvCreamBuns;
    FragmentContainerView cartContainter;
    CreamBunAdapter creamBunAdapter;

    MainViewModel vm;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = new ViewModelProvider(this).get(MainViewModel.class);
        vm.initializeVM(this, Volley.newRequestQueue(this));

        cartContainter = findViewById(R.id.fmcMainCart);

        creamBunAdapter = new CreamBunAdapter(this, this);
        rcvCreamBuns = findViewById(R.id.rcvMainCreamBuns);
        rcvCreamBuns.setLayoutManager(new GridLayoutManager(this, 3));
        rcvCreamBuns.setAdapter(creamBunAdapter);
        creamBunAdapter.updateCreamBunList(vm.getCreamBuns().getValue(), true);

        initTopbar(vm.getCurrentUser().getValue());
        vm.getCurrentUser().observe(this, this::initTopbar);
        vm.getCreamBuns().observe(this, creamBuns -> {
            User currentUser = vm.getCurrentUser().getValue();
            if(currentUser != null)
                creamBunAdapter.updateCreamBunList(creamBuns, currentUser.isAdmin);
            else
                creamBunAdapter.updateCreamBunList(creamBuns, false);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "onCreate: 1");
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d(TAG, "onCreate: 2");
                        vm.fetchCreamBuns();
                    }
                });

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fmcMainCart, CartFragment.newInstance())
                .commitNow();

        vm.getCart().observe(this, cart -> {
            if (cart.size() >= 1) {
                cartContainter.setVisibility(View.VISIBLE);
            }
            else {
                cartContainter.setVisibility(View.GONE);
            }
        });


    }

    private void initTopbar(User currentUser) {

        Log.d(TAG, "initTopbar: " + currentUser);
        if(currentUser == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fmcMainTopbar, TopbarFragment.newInstance("LOGIN"))
                    .commitNow();
        }
        else {
            creamBunAdapter.updateCreamBunList(vm.getCreamBuns().getValue(), currentUser.isAdmin);
            if (currentUser.isAdmin) {
                startService(new Intent(this, OrderService.class));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fmcMainTopbar, TopbarFragment.newInstance("ORDERS"))
                        .commitNow();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fmcMainTopbar, TopbarFragment.newInstance("ACCOUNT"))
                        .commitNow();
            }
        }
    }

    @Override
    public void onCreamBunClicked(int index) {
        Log.d(TAG, "onCreamBunClicked: " + index);
        if (vm.getCurrentUser().getValue() != null) {
            if (index == -1 && vm.getCurrentUser().getValue().isAdmin) {
                launcher.launch(new Intent(this, AddBunActivity.class));
            } else if (!vm.getCurrentUser().getValue().isAdmin){
                vm.addToCart(index);
            }
        }
    }
}
