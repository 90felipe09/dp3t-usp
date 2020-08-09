package com.example.dp3t_usp.APIService;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Function;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public interface APIService {

    public interface onGetHashesSuccessCallback {
        public void callback(ArrayList<String> hashes);
    }

    public interface onPostHashesSuccessCallback extends OnSuccessListener {
        public void callback();
    }

    interface onPostHashesFailureCallback extends OnFailureListener{
        public void callback();
    }

    public void sendHashes(ArrayList<String> hashes, final onPostHashesSuccessCallback successCallback, final onPostHashesFailureCallback failureCallback);

    public void getInfectedHashes(final onGetHashesSuccessCallback successCallback);

}
