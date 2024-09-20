package com.future.getd.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.future.getd.net.model.Message;

import java.util.List;

@Dao
public interface  MessageDao {
    @Query("SELECT * FROM chat_message_table")
    List<Message> getAll();

    @Query("SELECT * FROM chat_message_table where account = :account")
    List<Message> getAll(String account);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Message... messages);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Message> messages);

    @Delete
    void delete(Message message);

    @Query("Delete FROM chat_message_table where id = :id")
    void deleteById(String id);

    @Query("Delete FROM chat_message_table")
    void deleteAll();

    @Update
    void update(Message... messages);
}
