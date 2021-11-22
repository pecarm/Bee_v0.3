package com.example.bee_v03;

import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.location.Location;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BeeRepository {

    private HivesLocationDao locationDao;
    private HiveDao hiveDao;
    private RecordDao recordDao;

    private LiveData<List<HivesLocation>> allLocations;
    private LiveData<List<Hive>> allHives;
    private LiveData<List<Record>> allRecords;

    //region Constructor
    public BeeRepository(Application application) {
        BeeDatabase database = BeeDatabase.getInstance(application);

        locationDao = database.locationDao();
        hiveDao = database.hiveDao();
        recordDao = database.recordDao();

        allLocations = locationDao.getLocations();
        allHives = hiveDao.getAllHives();
        allRecords = recordDao.getAllRecords();
    }
    //endregion

    //region Location methods
    public void insert(HivesLocation location) {
        new InsertLocationAsyncTask(locationDao).execute(location);
    }

    public void update(HivesLocation location) {
        new UpdateLocationAsyncTask(locationDao).execute(location);
    }

    public void delete(HivesLocation location) {
        new DeleteLocationAsyncTask(locationDao).execute(location);
    }

    public LiveData<List<HivesLocation>> getAllLocations() {
        return allLocations;
    }

    public LiveData<List<HivesLocation>> getLocationIdByName(String name) {
        return locationDao.getLocationsByName(name);
    }
    //endregion

    //region Hive methods
    public void insert(Hive hive) {
        new InsertHiveAsyncTask(hiveDao).execute(hive);
    }

    public void update(Hive hive) {
        new UpdateHiveAsyncTask(hiveDao).execute(hive);
    }

    public void delete(Hive hive) {
        new DeleteHiveAsyncTask(hiveDao).execute(hive);
    }

    public LiveData<List<Hive>> getAllHives() {
        return allHives;
    }

    public LiveData<List<Hive>> getHivesByLocationId(int id) {
        return hiveDao.getHivesByLocation(id);
    }
    //endregion

    //region Record methods
    public void insert(Record record) {
        new InsertRecordAsyncTask(recordDao).execute(record);
    }

    public void update(Record record) {
        new UpdateRecordAsyncTask(recordDao).execute(record);
    }

    public void delete(Record record) {
        new DeleteRecordAsyncTask(recordDao).execute(record);
    }

    public LiveData<List<Record>> getAllRecords() {
        return allRecords;
    }

    public LiveData<List<Record>> getRecordsByHiveId(int id) {
        return recordDao.getRecordsByHive(id);
    }

    public LiveData<List<Record>> getRecordsByLocationId(int id) {
        return recordDao.getRecordsByLocation(id);
    }
    //endregion

    //region Async hiveLocation methods
    private static class InsertLocationAsyncTask extends AsyncTask<HivesLocation, Void, Void> {
        private HivesLocationDao locationDao;

        private InsertLocationAsyncTask(HivesLocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(HivesLocation... locations) {
            locationDao.insert(locations[0]);
            return null;
        }
    }

    private static class UpdateLocationAsyncTask extends AsyncTask<HivesLocation, Void, Void> {
        private HivesLocationDao locationDao;

        private UpdateLocationAsyncTask(HivesLocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(HivesLocation... locations) {
            locationDao.update(locations[0]);
            return null;
        }
    }

    private static class DeleteLocationAsyncTask extends AsyncTask<HivesLocation, Void, Void> {
        private HivesLocationDao locationDao;

        private DeleteLocationAsyncTask(HivesLocationDao locationDao) {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(HivesLocation... locations) {
            locationDao.delete(locations[0]);
            return null;
        }
    }
    //endregion

    //region Async hive methods
    private static class InsertHiveAsyncTask extends AsyncTask<Hive ,Void, Void> {
        private HiveDao hiveDao;

        private InsertHiveAsyncTask(HiveDao hiveDao) {
            this.hiveDao = hiveDao;
        }

        @Override
        protected Void doInBackground(Hive... hives) {
            hiveDao.insert(hives[0]);
            return null;
        }
    }

    private static class UpdateHiveAsyncTask extends AsyncTask<Hive ,Void, Void> {
        private HiveDao hiveDao;

        private UpdateHiveAsyncTask(HiveDao hiveDao) {
            this.hiveDao = hiveDao;
        }

        @Override
        protected Void doInBackground(Hive... hives) {
            hiveDao.update(hives[0]);
            return null;
        }
    }

    private static class DeleteHiveAsyncTask extends AsyncTask<Hive ,Void, Void> {
        private HiveDao hiveDao;

        private DeleteHiveAsyncTask(HiveDao hiveDao) {
            this.hiveDao = hiveDao;
        }

        @Override
        protected Void doInBackground(Hive... hives) {
            hiveDao.delete(hives[0]);
            return null;
        }
    }
    //endregion

    //region Async record methods
    private static class InsertRecordAsyncTask extends AsyncTask<Record ,Void, Void> {
        private RecordDao recordDao;

        private InsertRecordAsyncTask(RecordDao recordDao) {
            this.recordDao = recordDao;
        }

        @Override
        protected Void doInBackground(Record... records) {
            recordDao.insert(records[0]);
            return null;
        }
    }

    private static class UpdateRecordAsyncTask extends AsyncTask<Record ,Void, Void> {
        private RecordDao recordDao;

        private UpdateRecordAsyncTask(RecordDao recordDao) {
            this.recordDao = recordDao;
        }

        @Override
        protected Void doInBackground(Record... records) {
            recordDao.update(records[0]);
            return null;
        }
    }

    private static class DeleteRecordAsyncTask extends AsyncTask<Record ,Void, Void> {
        private RecordDao recordDao;

        private DeleteRecordAsyncTask(RecordDao recordDao) {
            this.recordDao = recordDao;
        }

        @Override
        protected Void doInBackground(Record... records) {
            recordDao.delete(records[0]);
            return null;
        }
    }
    //endregion
}
