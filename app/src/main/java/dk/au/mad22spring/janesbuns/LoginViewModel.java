package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.User;

public class LoginViewModel extends ViewModel {
    Repository repo;

    public void initializeVM() { repo = Repository.getInstance(); }

    public void updateCurrentUser(String uid) { repo.updateCurrentUser(uid); }
}
