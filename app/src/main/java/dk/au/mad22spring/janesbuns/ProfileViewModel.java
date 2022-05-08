package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.janesbuns.models.Currency;
import dk.au.mad22spring.janesbuns.models.User;

public class ProfileViewModel extends ViewModel {

    Repository repo;
    MutableLiveData<User> currentUser;
    MutableLiveData<List<String>> currencyCodes;

    public void initializeVM(LifecycleOwner lifecycleOwner) {
        repo = Repository.getInstance();
        repo.getCurrentUser().observe(lifecycleOwner, currentUser -> this.currentUser.setValue(currentUser));
        repo.getCurrencyCodes().observe(lifecycleOwner, currencyCodes -> this.currencyCodes.setValue(currencyCodes));
    }

    public LiveData<User> getCurrentUser () {
        if (currentUser == null) {
            currentUser = new MutableLiveData<User>();
        }

        return currentUser;
    }

    public LiveData<List<String>> getCurrencyCodes() {
        if (currencyCodes == null) {
            currencyCodes = new MutableLiveData<>(new ArrayList<>());
        }

        return currencyCodes;
    }
}
