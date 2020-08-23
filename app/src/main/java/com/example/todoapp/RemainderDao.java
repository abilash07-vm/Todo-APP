package com.example.todoapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RemainderDao {
    @Insert
    void addRemainder(Remainder remainder);

    @Delete
    void deleteRemainder(Remainder remainder);

    @Query("select * from remainder where iscomplete=:iscomplete order by date desc,t24hrtime desc")
    List<Remainder> getALlRemaindersByComplete(boolean iscomplete);

    @Query("select * from remainder where iscomplete=:iscomplete order by date asc,t24hrtime asc")
    List<Remainder> getALlRemaindersByCompleteBySmall(boolean iscomplete);

    @Query("update remainder set iscomplete=:iscomplete where id=:id")
    void setCompleted(int id,boolean iscomplete);

}
