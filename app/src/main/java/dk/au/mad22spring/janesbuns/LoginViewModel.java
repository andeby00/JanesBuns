package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    Repository repo;

    public void initializeVM() { repo = Repository.getInstance(); }

    public void updateCurrentUser(String uid) { repo.updateCurrentUser(uid); }
}
