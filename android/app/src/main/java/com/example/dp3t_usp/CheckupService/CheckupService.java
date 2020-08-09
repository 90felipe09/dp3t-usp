package com.example.dp3t_usp.CheckupService;


import android.content.Context;
import android.database.Cursor;

import com.example.dp3t_usp.DBService.DBInfectedHashes.InfectedHashesContract;
import com.example.dp3t_usp.DBService.DBInfectedHashes.InfectedHashesData;
import com.example.dp3t_usp.DBService.DBInfectedHashes.InfectedHashesService;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesContract;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesData;
import com.example.dp3t_usp.DBService.DBListenedHashes.ListenedHashesService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CheckupService {
    private static final int HASH_TOLERANCE = 5;
    private Context context;

    public CheckupService(Context context){
        this.context = context;
    }

    public boolean wasExposed(){
        ListenedHashesService listenedHashesService = new ListenedHashesService(context);
        InfectedHashesService infectedHashesService = new InfectedHashesService(context);

        ArrayList<ListenedHashesData> listenedHashesList = listenedHashesService.getData();
        ArrayList<InfectedHashesData> infectedHashesList = infectedHashesService.getData();

        String listenedHashColumn = ListenedHashesContract.COLUMN_LISTENED_HASH;
        String infectedHashColumn = InfectedHashesContract.COLUMN_INFECTED_HASH;

        Iterator<ListenedHashesData> listenedIterator = listenedHashesList.iterator();
        Iterator<InfectedHashesData> infectedIterator = infectedHashesList.iterator();

        HashMap<String, Integer> hashmapCollision = new HashMap<>();

        while(listenedIterator.hasNext()){
            hashmapCollision.put(listenedIterator.next().getField(listenedHashColumn), 0);
        }

        String currentHash;
        int currentValue;
        while(infectedIterator.hasNext()){
            currentHash = infectedIterator.next().getField(infectedHashColumn);
            if (hashmapCollision.containsKey(currentHash)){
                currentValue = hashmapCollision.get(currentHash);
                currentValue++;
                hashmapCollision.put(currentHash, currentValue);
            }
        }

        Collection<Integer> collisionValues = hashmapCollision.values();

        Iterator<Integer> collisionIterator = collisionValues.iterator();

        int accumulatedCollisions = 0;
        while(collisionIterator.hasNext()){
            accumulatedCollisions += collisionIterator.next();
        }
        if(accumulatedCollisions >= HASH_TOLERANCE){
            return true;
        }
        else{
            return false;
        }

    }

}
