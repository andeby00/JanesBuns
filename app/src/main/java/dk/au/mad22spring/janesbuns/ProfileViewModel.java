package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dk.au.mad22spring.janesbuns.models.User;

public class ProfileViewModel extends ViewModel {

    Repository repo;
    MutableLiveData<User> currentUser;

    public void initializeVM(LifecycleOwner lifecycleOwner) {
        repo = Repository.getInstance();
        repo.getCurrentUser().observe(lifecycleOwner, currentUser -> this.currentUser.setValue(currentUser));
    }

    public LiveData<User> getCurrentUser () {
        if (currentUser == null) {
            currentUser = new MutableLiveData<User>();
        }

        return currentUser;
    }
}
