package dk.au.mad22spring.janesbuns;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.Currencies;
import dk.au.mad22spring.janesbuns.models.Currency;
import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;
import dk.au.mad22spring.janesbuns.utils.CurrencyAPI;

public class Repository implements CurrencyAPI.IApiCallback {
    static final String TAG = "Repository";
    static Repository instance;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    CurrencyAPI api;

    MutableLiveData<List<CreamBun>> creamBuns;
    MutableLiveData<List<CreamBun>> cart;
    MutableLiveData<List<Order>> orders;
    MutableLiveData<User> currentUser = null;

    private Repository(RequestQueue queue) {
        creamBuns = new MutableLiveData<>(new ArrayList<>());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        api = new CurrencyAPI();

        String tempUid = null;

        if (mAuth.getCurrentUser() != null) {
            tempUid = mAuth.getCurrentUser().getUid();
            updateCurrentUser(tempUid);
        }

        fetchCreamBuns();

//        api.sendRequest("https://api.currencyapi.com/v3/currencies?apikey=ryw2Tadc8qURWy5QTQ5Kq1zoQAtvh0kpGZqfCGO1&currencies=", queue, this);
    }

    public Repository() {
    }

    public void fetchCreamBuns() {
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

    public static Repository getInstance(RequestQueue queue) {
        if (instance == null) {
            instance = new Repository(queue);
        }

        return instance;
    }


    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }

        return instance;
    }

    public LiveData<List<CreamBun>> getCreamBuns() {
        return creamBuns;
    }

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

    public void clearCart() {
        cart.setValue(new ArrayList<>());
    }

    public void sendCurrencyRequest(String url, RequestQueue queue, CurrencyAPI.IApiCallback callback) {
        api.sendRequest(url, queue, callback);
    }

    @Override
    public void apiHandler(String response) {
        Log.d(TAG, "apiHandler: " + response);
        try {
            Currencies result = new ObjectMapper().readValue(response, Currencies.class);

            for (Map.Entry<String, Currency> entry : result.data.entrySet()) {
                String key = entry.getKey();
                Currency currency = entry.getValue();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}