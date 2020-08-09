package com.example.dp3t_usp.APIService;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAPIService implements APIService {

    private static final String TAG = "FIREBASE_API";
    private FirebaseFirestore db;

    public FirebaseAPIService(){
        Log.e(" start api", " startin");
        this.db = FirebaseFirestore.getInstance();
    }

    public void sendHashes(ArrayList<String> hashes, final onPostHashesSuccessCallback successCallback, final onPostHashesFailureCallback failureCallback){
        this.db.collection("hashes-lists").document("hashes");

        Map<String, Object> data = new HashMap<>();
        data.put("hashes", hashes);
        db.collection("hashes-lists")
                .add(data)
                .addOnSuccessListener(successCallback)
                .addOnFailureListener(failureCallback);
    }

    public void getInfectedHashes(final onGetHashesSuccessCallback successCallback ){

        final ArrayList hashes = new ArrayList();
        this.db.collection("hashes-lists")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                hashes.addAll((ArrayList) document.getData().get("hashes"));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            successCallback.callback(hashes);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
