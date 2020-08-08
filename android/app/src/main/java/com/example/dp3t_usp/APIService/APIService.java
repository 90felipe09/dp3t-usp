package com.example.dp3t_usp.APIService;

import com.google.android.gms.tasks.Task;
import com.google.common.base.Function;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public interface APIService {

    public interface onGetHashesSuccessCallback {
        public void callback(ArrayList<String> hashes);
    }

    public void sendHashes(ArrayList<String> hashes);

    public void getInfectedHashes(final onGetHashesSuccessCallback successCallback);

}
