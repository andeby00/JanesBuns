package dk.au.mad22spring.janesbuns.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import dk.au.mad22spring.janesbuns.CreamBunAdapter;
import dk.au.mad22spring.janesbuns.MainViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.fragments.TopbarFragment;

public class MainActivity extends AppCompatActivity implements CreamBunAdapter.ICreamBunItemClickedListener {
    private static final String TAG = "MainActivity";
    
    RecyclerView rcvCreamBuns;
    CreamBunAdapter creamBunAdapter;

    MainViewModel vm;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fmcMainTopbar, TopbarFragment.newInstance("LOGIN"))
                    .commitNow();
        }

        vm = new ViewModelProvider(this).get(MainViewModel.class);
        vm.initializeVM(this);

        creamBunAdapter = new CreamBunAdapter(this);
        rcvCreamBuns = findViewById(R.id.rcvMainCreamBuns);
        rcvCreamBuns.setLayoutManager(new GridLayoutManager(this, 3));
        rcvCreamBuns.setAdapter(creamBunAdapter);
        creamBunAdapter.updateCreamBunList(vm.getCreamBuns().getValue());

        vm.getCreamBuns().observe(this, creamBuns -> creamBunAdapter.updateCreamBunList(creamBuns));

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
