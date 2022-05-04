package dk.au.mad22spring.janesbuns.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.au.mad22spring.janesbuns.R;
import dk.au.mad22spring.janesbuns.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopbarFragment extends Fragment {
    Button button;
    private static final String ARG_PARAM1 = "";

    private String btnType;

    public TopbarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param btnType Parameter 1.
     * @return A new instance of fragment TopbarFragment.
     */

    public static TopbarFragment newInstance(String btnType) {
        TopbarFragment fragment = new TopbarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, btnType);
        fragment.setArguments(args);
        return fragment;
    }

    public static TopbarFragment newInstance() {
        return new TopbarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            btnType = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_topbar, container, false);
        button = v.findViewById(R.id.btnTopbarButton);
        if(btnType == "LOGIN") {
            button.setOnClickListener(this::onClickLogin);
        }
        return v;
    }

    private void onClickLogin(View view) {
        startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
    }
}