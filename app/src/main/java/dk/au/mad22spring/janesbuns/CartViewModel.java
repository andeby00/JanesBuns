package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.User;

public class CartViewModel extends ViewModel {
    MutableLiveData<List<CreamBun>> cart;
    MutableLiveData<User> currentUser;
    Repository repo;

    public void initializeVM(LifecycleOwner lifecycleOwner) {
        currentUser = new MutableLiveData<User>();

        repo = Repository.getInstance();
        repo.getCart().observe(lifecycleOwner, cart -> this.cart.setValue(cart));
        repo.getCurrentUser().observe(lifecycleOwner, currentUser -> this.currentUser.setValue(currentUser));
    }

    public LiveData<List<CreamBun>> getCart() {
        if (cart == null) {
            cart = new MutableLiveData<>(new ArrayList<>());
        }

        return cart;
    }

    public LiveData<User> getCurrentUser() {
        if (currentUser == null) {
            currentUser = new MutableLiveData<User>();
        }

        return currentUser;
    }

    public void removeFromCart(int index) { repo.removeFromCart(index); }

    public void clearCart() { repo.clearCart(); }
}
