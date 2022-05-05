package dk.au.mad22spring.janesbuns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dk.au.mad22spring.janesbuns.models.CreamBun;
import dk.au.mad22spring.janesbuns.models.User;

public class MainViewModel extends ViewModel {
    MutableLiveData<List<CreamBun>> creamBuns;
    Repository repo;

    public void initializeVM(LifecycleOwner lifecycleOwner) {
        if (creamBuns != null) {
            return;
        }

        creamBuns = new MutableLiveData<>(new ArrayList<>());
        repo = Repository.getInstance();
        repo.getCreamBuns().observe(lifecycleOwner, creamBuns -> {
            this.creamBuns.setValue(creamBuns);
        });
    }

    public LiveData<List<CreamBun>> getCreamBuns() {
        if (creamBuns == null) {
            creamBuns = new MutableLiveData<>(new ArrayList<>());
        }

        return creamBuns;
    }

    public User getCurrentUser () { return repo.getCurrentUser(); }
}
