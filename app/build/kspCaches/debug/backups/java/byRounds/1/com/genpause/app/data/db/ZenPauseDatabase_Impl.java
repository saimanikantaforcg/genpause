package com.genpause.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.genpause.app.data.dao.EventDao;
import com.genpause.app.data.dao.EventDao_Impl;
import com.genpause.app.data.dao.ScheduleDao;
import com.genpause.app.data.dao.ScheduleDao_Impl;
import com.genpause.app.data.dao.TargetAppDao;
import com.genpause.app.data.dao.TargetAppDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ZenPauseDatabase_Impl extends ZenPauseDatabase {
  private volatile TargetAppDao _targetAppDao;

  private volatile EventDao _eventDao;

  private volatile ScheduleDao _scheduleDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `target_apps` (`packageName` TEXT NOT NULL, `displayName` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `baseDelaySeconds` INTEGER NOT NULL, `interventionType` TEXT NOT NULL, `hardModeEnabled` INTEGER NOT NULL, `snoozedUntilMillis` INTEGER NOT NULL, `escalationPolicy` TEXT NOT NULL, `reInterventionEnabled` INTEGER NOT NULL, `reInterventionIntervalMin` INTEGER NOT NULL, PRIMARY KEY(`packageName`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timestamp` INTEGER NOT NULL, `packageName` TEXT NOT NULL, `action` TEXT NOT NULL, `delaySeconds` INTEGER NOT NULL, `mode` TEXT NOT NULL, `promptId` TEXT, `reasonText` TEXT, `emotionId` TEXT, `intentionDurationMin` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `schedules` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `label` TEXT NOT NULL, `daysOfWeekMask` INTEGER NOT NULL, `startMinutes` INTEGER NOT NULL, `endMinutes` INTEGER NOT NULL, `strictnessLevel` TEXT NOT NULL, `enabled` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'de395a8dd81f4fa47d368f741ac44317')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `target_apps`");
        db.execSQL("DROP TABLE IF EXISTS `events`");
        db.execSQL("DROP TABLE IF EXISTS `schedules`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTargetApps = new HashMap<String, TableInfo.Column>(10);
        _columnsTargetApps.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("baseDelaySeconds", new TableInfo.Column("baseDelaySeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("interventionType", new TableInfo.Column("interventionType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("hardModeEnabled", new TableInfo.Column("hardModeEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("snoozedUntilMillis", new TableInfo.Column("snoozedUntilMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("escalationPolicy", new TableInfo.Column("escalationPolicy", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("reInterventionEnabled", new TableInfo.Column("reInterventionEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTargetApps.put("reInterventionIntervalMin", new TableInfo.Column("reInterventionIntervalMin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTargetApps = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTargetApps = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTargetApps = new TableInfo("target_apps", _columnsTargetApps, _foreignKeysTargetApps, _indicesTargetApps);
        final TableInfo _existingTargetApps = TableInfo.read(db, "target_apps");
        if (!_infoTargetApps.equals(_existingTargetApps)) {
          return new RoomOpenHelper.ValidationResult(false, "target_apps(com.genpause.app.data.entity.TargetAppEntity).\n"
                  + " Expected:\n" + _infoTargetApps + "\n"
                  + " Found:\n" + _existingTargetApps);
        }
        final HashMap<String, TableInfo.Column> _columnsEvents = new HashMap<String, TableInfo.Column>(10);
        _columnsEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("packageName", new TableInfo.Column("packageName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("action", new TableInfo.Column("action", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("delaySeconds", new TableInfo.Column("delaySeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("mode", new TableInfo.Column("mode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("promptId", new TableInfo.Column("promptId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("reasonText", new TableInfo.Column("reasonText", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("emotionId", new TableInfo.Column("emotionId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvents.put("intentionDurationMin", new TableInfo.Column("intentionDurationMin", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEvents = new TableInfo("events", _columnsEvents, _foreignKeysEvents, _indicesEvents);
        final TableInfo _existingEvents = TableInfo.read(db, "events");
        if (!_infoEvents.equals(_existingEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "events(com.genpause.app.data.entity.EventEntity).\n"
                  + " Expected:\n" + _infoEvents + "\n"
                  + " Found:\n" + _existingEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsSchedules = new HashMap<String, TableInfo.Column>(7);
        _columnsSchedules.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("label", new TableInfo.Column("label", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("daysOfWeekMask", new TableInfo.Column("daysOfWeekMask", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("startMinutes", new TableInfo.Column("startMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("endMinutes", new TableInfo.Column("endMinutes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("strictnessLevel", new TableInfo.Column("strictnessLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSchedules.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSchedules = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSchedules = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSchedules = new TableInfo("schedules", _columnsSchedules, _foreignKeysSchedules, _indicesSchedules);
        final TableInfo _existingSchedules = TableInfo.read(db, "schedules");
        if (!_infoSchedules.equals(_existingSchedules)) {
          return new RoomOpenHelper.ValidationResult(false, "schedules(com.genpause.app.data.entity.ScheduleEntity).\n"
                  + " Expected:\n" + _infoSchedules + "\n"
                  + " Found:\n" + _existingSchedules);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "de395a8dd81f4fa47d368f741ac44317", "1260ae7cc263b3e6ff47a387d4b641c6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "target_apps","events","schedules");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `target_apps`");
      _db.execSQL("DELETE FROM `events`");
      _db.execSQL("DELETE FROM `schedules`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TargetAppDao.class, TargetAppDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EventDao.class, EventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScheduleDao.class, ScheduleDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TargetAppDao targetAppDao() {
    if (_targetAppDao != null) {
      return _targetAppDao;
    } else {
      synchronized(this) {
        if(_targetAppDao == null) {
          _targetAppDao = new TargetAppDao_Impl(this);
        }
        return _targetAppDao;
      }
    }
  }

  @Override
  public EventDao eventDao() {
    if (_eventDao != null) {
      return _eventDao;
    } else {
      synchronized(this) {
        if(_eventDao == null) {
          _eventDao = new EventDao_Impl(this);
        }
        return _eventDao;
      }
    }
  }

  @Override
  public ScheduleDao scheduleDao() {
    if (_scheduleDao != null) {
      return _scheduleDao;
    } else {
      synchronized(this) {
        if(_scheduleDao == null) {
          _scheduleDao = new ScheduleDao_Impl(this);
        }
        return _scheduleDao;
      }
    }
  }
}
