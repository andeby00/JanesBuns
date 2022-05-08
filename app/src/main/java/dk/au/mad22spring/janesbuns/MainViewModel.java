package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.User;

public class MainViewModel extends ViewModel {
    MutableLiveData<List<CreamBun>> creamBuns;
    MutableLiveData<List<CreamBun>> cart;
    MutableLiveData<User> currentUser;
    Repository repo;

    public void initializeVM(LifecycleOwner lifecycleOwner, RequestQueue queue) {
        if (creamBuns != null) {
            return;
        }

        creamBuns = new MutableLiveData<>(new ArrayList<>());
        repo = Repository.getInstance(queue);
        repo.getCreamBuns().observe(lifecycleOwner, creamBuns -> this.creamBuns.setValue(creamBuns) );
        repo.getCurrentUser().observe(lifecycleOwner, currentUser -> this.currentUser.setValue(currentUser));
        repo.getCart().observe(lifecycleOwner, cart -> this.cart.setValue(cart));
    }

    public LiveData<List<CreamBun>> getCreamBuns() {
        if (creamBuns == null) {
            creamBuns = new MutableLiveData<>(new ArrayList<>());
        }

        return creamBuns;
    }

    public LiveData<User> getCurrentUser () {
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

    public void addToCart(int index) {
        repo.addToCart(index);
    }

    public void fetchCreamBuns() {
        repo.fetchCreamBuns();
    }
}
