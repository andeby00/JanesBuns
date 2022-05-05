package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.ViewModel;

import java.util.Optional;

import dk.au.mad22spring.janesbuns.models.User;

public class ProfileViewModel extends ViewModel {

    Repository repo;

    public void initializeVM() { repo = Repository.getInstance(); }

    public User getCurrentUser () { return repo.getCurrentUser(); }

}
