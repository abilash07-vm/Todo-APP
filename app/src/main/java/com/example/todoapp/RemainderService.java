package com.example.todoapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class RemainderService extends Service {
    private IBinder binder=new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder{
        RemainderService getService(){
            return RemainderService.this;
        }
    }
}
