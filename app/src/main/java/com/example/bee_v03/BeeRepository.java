package com.example.bee_v03;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BeeRepository {

    private HivesLocationDao locationDao;
    private HiveDao hiveDao;
    private RecordDao recordDao;
    private AlertDao alertDao;
    private HoneyHarvestDao honeyHarvestDao;

    private LiveData<List<HivesLocation>> allLocations;
    private LiveData<List<Hive>> allHives;
    private LiveData<List<Record>> allRecords;
    private LiveData<List<Alert>> allAlerts;
    private LiveData<List<HoneyHarvest>> allHoneyHarvests;

    //region Constructor
    public BeeRepository(Application application) {
        BeeDatabase database = BeeDatabase.getInstance(application);

        locationDao = database.locationDao();
        hiveDao = database.hiveDao();
        recordDao = database.recordDao();
        alertDao = database.alertDao();
        honeyHarvestDao = database.honeyHarvestDao();

        allLocations = locationDao.getAllLocations();
        allHives = hiveDao.getAllHives();
        allRecords = recordDao.getAllRecords();
        allAlerts = alertDao.getAllAlerts();
        allHoneyHarvests = honeyHarvestDao.getAllHoneyHarvests();
    }
    //endregion

    //region Methods
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

    public String getHiveName(int id) {
        return hiveDao.getHiveName(id);
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

    //region Alert methods
    public void insert(Alert alert) {
        new InsertAlertAsyncTask(alertDao).execute(alert);
    }

    public void update(Alert alert) {
        new UpdateAlertAsyncTask(alertDao).execute(alert);
    }

    public void delete(Alert alert) {
        new DeleteAlertAsyncTask(alertDao).execute(alert);
    }

    public LiveData<List<Alert>> getAllAlerts() {
        return allAlerts;
    }
    //endregion

    //region Honey harvest methods
    public void insert(HoneyHarvest harvest) {
        new InsertHoneyHarvestAsyncTask(honeyHarvestDao).execute(harvest);
    }

    public void update(HoneyHarvest harvest) {
        new UpdateHoneyHarvestAsyncTask(honeyHarvestDao).execute(harvest);
    }

    public void delete(HoneyHarvest harvest) {
        new DeleteHoneyHarvestAsyncTask(honeyHarvestDao).execute(harvest);
    }

    public LiveData<List<HoneyHarvest>> getAllHoneyHarvests() {
        return allHoneyHarvests;
    }
    //endregion
    //endregion

    //region Async methods
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
    private static class InsertRecordAsyncTask extends AsyncTask<Record, Void, Void> {
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

    private static class UpdateRecordAsyncTask extends AsyncTask<Record, Void, Void> {
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

    private static class DeleteRecordAsyncTask extends AsyncTask<Record, Void, Void> {
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

    //region Async alert methods
    private static class InsertAlertAsyncTask extends AsyncTask<Alert, Void, Void> {
        private AlertDao alertDao;

        private InsertAlertAsyncTask(AlertDao alertDao) {
            this.alertDao = alertDao;
        }

        @Override
        protected Void doInBackground(Alert... alerts) {
            alertDao.insert(alerts[0]);
            return null;
        }
    }

    private static class UpdateAlertAsyncTask extends AsyncTask<Alert, Void, Void> {
        private AlertDao alertDao;

        private UpdateAlertAsyncTask(AlertDao alertDao) {
            this.alertDao = alertDao;
        }

        @Override
        protected Void doInBackground(Alert... alerts) {
            alertDao.update(alerts[0]);
            return null;
        }
    }

    private static class DeleteAlertAsyncTask extends AsyncTask<Alert, Void, Void> {
        private AlertDao alertDao;

        private DeleteAlertAsyncTask(AlertDao alertDao) {
            this.alertDao = alertDao;
        }

        @Override
        protected Void doInBackground(Alert... alerts) {
            alertDao.delete(alerts[0]);
            return null;
        }
    }
    //endregion

    //region Async honeyHarvest methods
    private static class InsertHoneyHarvestAsyncTask extends AsyncTask<HoneyHarvest, Void, Void> {
        private HoneyHarvestDao honeyHarvestDao;

        private InsertHoneyHarvestAsyncTask(HoneyHarvestDao honeyHarvestDao) {
            this.honeyHarvestDao = honeyHarvestDao;
        }

        @Override
        protected Void doInBackground(HoneyHarvest... harvests) {
            honeyHarvestDao.insert(harvests[0]);
            return null;
        }
    }

    private static class UpdateHoneyHarvestAsyncTask extends AsyncTask<HoneyHarvest, Void, Void> {
        private HoneyHarvestDao honeyHarvestDao;

        private UpdateHoneyHarvestAsyncTask(HoneyHarvestDao honeyHarvestDao) {
            this.honeyHarvestDao = honeyHarvestDao;
        }

        @Override
        protected Void doInBackground(HoneyHarvest... harvests) {
            honeyHarvestDao.update(harvests[0]);
            return null;
        }
    }

    private static class DeleteHoneyHarvestAsyncTask extends AsyncTask<HoneyHarvest, Void, Void> {
        private HoneyHarvestDao honeyHarvestDao;

        private DeleteHoneyHarvestAsyncTask(HoneyHarvestDao honeyHarvestDao) {
            this.honeyHarvestDao = honeyHarvestDao;
        }

        @Override
        protected Void doInBackground(HoneyHarvest... harvests) {
            honeyHarvestDao.delete(harvests[0]);
            return null;
        }
    }
    //endregion
    //endregion
}
