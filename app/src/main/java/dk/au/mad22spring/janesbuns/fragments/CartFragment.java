package dk.au.mad22spring.janesbuns.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import dk.au.mad22spring.janesbuns.MainViewModel;
import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.activities.CartActivity;

public class CartFragment extends Fragment {

    Button button;
    TextView text;
    MainViewModel mainVM;

    public CartFragment() {
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        text = v.findViewById(R.id.txtCartText);
        button = v.findViewById(R.id.btnCartToCart);

        button.setOnClickListener(view -> startActivity(new Intent(getActivity(), CartActivity.class)));

        mainVM = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainVM.getCart().observe(getActivity(), creamBuns -> {
            text.setText(getString(R.string.itemsInCart) + creamBuns.size());
        });

        return v;
    }
}