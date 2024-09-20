package com.future.getd.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.future.getd.database.dao.MessageDao;
import com.future.getd.database.dao.UserDao;
import com.future.getd.database.entity.UserEntity;
import com.future.getd.net.model.Message;


@Database(entities = {UserEntity.class, Message.class},version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase INSTANCE;

    // 因为需要一个Context，所有我们得传来一个Context
    public static synchronized AppDataBase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "app_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()////数据库更新时删除数据重新创建
                    .allowMainThreadQueries()//test
//                    .addMigrations(MIGRATION_1_2)//指定版本1-2升级时的升级策略
                    .build();
        }
        return INSTANCE;
    }

    public static AppDataBase getInstance() {
        return INSTANCE;
    }

    public abstract UserDao getUserDao();
    public abstract MessageDao getChatDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //将数据表device创建出来
//            database.execSQL("CREATE TABLE 'device' ('id'  TEXT NOT NULL,'location' TEXT,'deviceName' TEXT,'deviceType' TEXT,PRIMARY KEY ('id')) ");
        }
    };
}
