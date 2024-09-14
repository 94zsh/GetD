package com.future.getd.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.future.getd.database.entity.BindDevice;

import java.util.List;

public class DataRepository {
//    private WordDao mWordDao;
    private LiveData<List<BindDevice>> mAllDevices;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public DataRepository(Application application) {
//        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
//        mWordDao = db.wordDao();
//        mAllWords = mWordDao.getAlphabetizedWords();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<BindDevice>> getAllBindDevices() {
        return mAllDevices;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(BindDevice device) {
//        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
//            mWordDao.insert(word);
//        });
    }
}
