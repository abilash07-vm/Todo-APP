package com.example.todoapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = Remainder.class,version = 1,exportSchema =false)
public abstract class RemainderDatabase extends RoomDatabase {
    private static RemainderDatabase instance;
    public abstract RemainderDao remainderDao();
    public static synchronized RemainderDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context,
                    RemainderDatabase.class,
                    "Remainder.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


}
