package dk.au.mad22spring.janesbuns.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Optional;
import java.util.concurrent.BlockingDeque;

import dk.au.mad22spring.janesbuns.CreamBunAdapter;
import dk.au.mad22spring.janesbuns.MainViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.fragments.TopbarFragment;
import dk.au.mad22spring.janesbuns.models.User;

public class MainActivity extends AppCompatActivity implements CreamBunAdapter.ICreamBunItemClickedListener {
    private static final String TAG = "MainActivity";
    
    RecyclerView rcvCreamBuns;
    CreamBunAdapter creamBunAdapter;

    MainViewModel vm;
    ActivityResultLauncher<Intent> launcher;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vm = new ViewModelProvider(this).get(MainViewModel.class);
        vm.initializeVM(this);

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: " + mAuth.getCurrentUser().getEmail());

        creamBunAdapter = new CreamBunAdapter(this);
        rcvCreamBuns = findViewById(R.id.rcvMainCreamBuns);
        rcvCreamBuns.setLayoutManager(new GridLayoutManager(this, 3));
        rcvCreamBuns.setAdapter(creamBunAdapter);
        creamBunAdapter.updateCreamBunList(vm.getCreamBuns().getValue(), true);

        vm.getCreamBuns().observe(this, creamBuns -> creamBunAdapter.updateCreamBunList(creamBuns, true));

        if (vm.getCurrentUser().isPresent())
            Log.d(TAG, "onCreate: " + vm.getCurrentUser().get().fullName);
            //Toast.makeText(this, vm.getCurrentUser().get().fullName, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTopbar();
    }

    private void initTopbar() {
        Optional<User> currentUser = vm.getCurrentUser();

        Log.d(TAG, "initTopbar: " + currentUser);
        if(!currentUser.isPresent()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fmcMainTopbar, TopbarFragment.newInstance("LOGIN"))
                    .commitNow();
        }
        else {
            if (currentUser.get().isAdmin) {
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
//        Intent intent = new Intent(this, DetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("Drink", vm.getDrinks().getValue().get(index));
//        bundle.putInt("Index", index);
//        intent.putExtras(bundle);
//        launcher.launch(intent);
    }
}
