package dk.au.mad22spring.janesbuns;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.Currency;
import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;

public class Repository {
    static final String TAG = "Repository";
    static Repository instance;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    RequestQueue queue;
    MutableLiveData<List<CreamBun>> creamBuns;
    MutableLiveData<List<CreamBun>> cart;
    MutableLiveData<List<Order>> orders;
    MutableLiveData<User> currentUser = null;

    private Repository() {
        creamBuns = new MutableLiveData<>(new ArrayList<>());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String tempUid = null;

        if (mAuth.getCurrentUser() != null) {
            tempUid = mAuth.getCurrentUser().getUid();
            updateCurrentUser(tempUid);
        }

        fetchCreamBuns();
    }

    public void fetchCreamBuns () {
        db.collection("creamBuns")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<CreamBun> tempCreamBuns = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tempCreamBuns.add(document.toObject(CreamBun.class));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }

                        creamBuns.setValue(tempCreamBuns);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void fetchOrders () {
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Order> tempOrders = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tempOrders.add(document.toObject(Order.class));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }

                        orders.setValue(tempOrders);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public static Repository getInstance() {
        if(instance==null){
            instance = new Repository();
        }
        return instance;
    }

    public LiveData<List<CreamBun>> getCreamBuns() { return creamBuns; }

    public void updateCurrentUser(String uid) {
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            currentUser.setValue(documentSnapshot.toObject(User.class));
        });
        Log.d(TAG, "updateCurrentUser: ");
    }

    public LiveData<User> getCurrentUser() {
        if (currentUser == null) {
            currentUser = new MutableLiveData<User>();
        }

        return currentUser;
    }

    public LiveData<List<CreamBun>> getCart() {
        if (cart == null) {
            cart = new MutableLiveData<>(new ArrayList<>());
        }

        return cart;
    }

    public LiveData<List<Order>> getOrders() {
        if (orders == null) {
            orders = new MutableLiveData<>(new ArrayList<>());
        }

        return orders;
    }

    public void addToCart(int index) {
        List<CreamBun> tempList = getCart().getValue();
        tempList.add(creamBuns.getValue().get(index));

        cart.setValue(tempList);
    }

    public void removeFromCart(int index) {
        List<CreamBun> tempList = getCart().getValue();
        if (tempList != null) {
            tempList.remove(index);
        }

        cart.setValue(tempList);
    }

    public void requestCurrency(MutableLiveData<List<Currency>> currencyList) {
        StringRequest mRequest = new StringRequest(
                Request.Method.GET,
                "https://free.currconv.com/api/v7/currencies?apiKey=8c339bcbe21bcd7cbb1b",
                response -> handleResponse(response, currencyList),
                error -> Log.e(TAG, "that did not work", error )
        );
        queue.add(mRequest);
    }

    private void handleResponse(String response, MutableLiveData<List<Currency>> currencyList) {
        List<Currency> tempCurrency = parseJson(response);
        currencyList.setValue(tempCurrency);
    }

    public static List<Currency> parseJson(String json){
        Gson gson = new GsonBuilder().create();
        Currency currency = gson.fromJson(json, Currency.class);
        return currency.currencies;
    }


    public void clearCart() {
        cart.setValue(new ArrayList<>());
    }
}
