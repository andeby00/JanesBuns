package dk.au.mad22spring.janesbuns;

import static dk.au.mad22spring.janesbuns.Repository.TAG;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.Order;
import dk.au.mad22spring.janesbuns.models.User;

public class OrderViewModel extends ViewModel {

    MutableLiveData<List<Order>> orders;
    MutableLiveData<User> currentUser;
    Repository repo;
    boolean isAdmin;

    public void initializeVM(LifecycleOwner lifecycleOwner, boolean isAdmin) {
        this.isAdmin = isAdmin;

        currentUser = new MutableLiveData<User>();

        repo = Repository.getInstance();

        repo.getCurrentUser().observe(lifecycleOwner, currentUser -> this.currentUser.setValue(currentUser));
        if(isAdmin) {
            repo.fetchOrders();
            repo.getOrders().observe(lifecycleOwner, orders -> this.orders.setValue(orders));
        }
    }

    public LiveData<List<Order>> getOrders() {
        if (orders == null) {
            orders = new MutableLiveData<>(new ArrayList<>());
        }

        return orders;
    }

    public LiveData<User> getCurrentUser() {
        if (currentUser == null) {
            currentUser = new MutableLiveData<User>();
        }

        return currentUser;
    }
}
