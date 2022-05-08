package dk.au.mad22spring.janesbuns;

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

    public void initializeVM(LifecycleOwner lifecycleOwner) {
        currentUser = new MutableLiveData<User>();

        repo = Repository.getInstance();
        repo.fetchOrders();
        repo.getOrders().observe(lifecycleOwner, orders -> this.orders.setValue(orders));
        repo.getCurrentUser().observe(lifecycleOwner, currentUser -> this.currentUser.setValue(currentUser));
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
