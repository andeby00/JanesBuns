package dk.au.mad22spring.janesbuns;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad22spring.janesbuns.models.CreamBun;

public class Repository {
    private static final String TAG = "Repository";

    //private final ExecutorService executor;
    //private final Api api;
    FirebaseFirestore db;
    private static Repository instance;

    private MutableLiveData<List<CreamBun>> creamBuns;

    private Repository() {
        //executor = Executors.newSingleThreadExecutor();
        //api = new Api(app.getApplicationContext());
        creamBuns = new MutableLiveData<>(new ArrayList<>());

        db = FirebaseFirestore.getInstance();

        db.collection("creamBuns")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<CreamBun> tempCreamBuns = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                tempCreamBuns.add(document.toObject(CreamBun.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }

                            creamBuns.setValue(tempCreamBuns);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static Repository getInstance() {
        if(instance==null){
            instance = new Repository();
        }
        return instance;
    }

    public LiveData<List<CreamBun>> getCreamBuns() { return creamBuns; }
}
