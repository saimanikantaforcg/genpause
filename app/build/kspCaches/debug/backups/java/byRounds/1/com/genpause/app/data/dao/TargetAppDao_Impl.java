package com.genpause.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.genpause.app.data.entity.TargetAppEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TargetAppDao_Impl implements TargetAppDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TargetAppEntity> __insertionAdapterOfTargetAppEntity;

  private final EntityDeletionOrUpdateAdapter<TargetAppEntity> __deletionAdapterOfTargetAppEntity;

  private final EntityDeletionOrUpdateAdapter<TargetAppEntity> __updateAdapterOfTargetAppEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByPackage;

  private final SharedSQLiteStatement __preparedStmtOfSnoozeApp;

  private final SharedSQLiteStatement __preparedStmtOfSetEnabled;

  public TargetAppDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTargetAppEntity = new EntityInsertionAdapter<TargetAppEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `target_apps` (`packageName`,`displayName`,`enabled`,`baseDelaySeconds`,`interventionType`,`hardModeEnabled`,`snoozedUntilMillis`,`escalationPolicy`,`reInterventionEnabled`,`reInterventionIntervalMin`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TargetAppEntity entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getDisplayName());
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getBaseDelaySeconds());
        statement.bindString(5, entity.getInterventionType());
        final int _tmp_1 = entity.getHardModeEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        statement.bindLong(7, entity.getSnoozedUntilMillis());
        statement.bindString(8, entity.getEscalationPolicy());
        final int _tmp_2 = entity.getReInterventionEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getReInterventionIntervalMin());
      }
    };
    this.__deletionAdapterOfTargetAppEntity = new EntityDeletionOrUpdateAdapter<TargetAppEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `target_apps` WHERE `packageName` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TargetAppEntity entity) {
        statement.bindString(1, entity.getPackageName());
      }
    };
    this.__updateAdapterOfTargetAppEntity = new EntityDeletionOrUpdateAdapter<TargetAppEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `target_apps` SET `packageName` = ?,`displayName` = ?,`enabled` = ?,`baseDelaySeconds` = ?,`interventionType` = ?,`hardModeEnabled` = ?,`snoozedUntilMillis` = ?,`escalationPolicy` = ?,`reInterventionEnabled` = ?,`reInterventionIntervalMin` = ? WHERE `packageName` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TargetAppEntity entity) {
        statement.bindString(1, entity.getPackageName());
        statement.bindString(2, entity.getDisplayName());
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.getBaseDelaySeconds());
        statement.bindString(5, entity.getInterventionType());
        final int _tmp_1 = entity.getHardModeEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        statement.bindLong(7, entity.getSnoozedUntilMillis());
        statement.bindString(8, entity.getEscalationPolicy());
        final int _tmp_2 = entity.getReInterventionEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_2);
        statement.bindLong(10, entity.getReInterventionIntervalMin());
        statement.bindString(11, entity.getPackageName());
      }
    };
    this.__preparedStmtOfDeleteByPackage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM target_apps WHERE packageName = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSnoozeApp = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE target_apps SET snoozedUntilMillis = ? WHERE packageName = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE target_apps SET enabled = ? WHERE packageName = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertApp(final TargetAppEntity app, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTargetAppEntity.insert(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertApps(final List<TargetAppEntity> apps,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTargetAppEntity.insert(apps);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteApp(final TargetAppEntity app, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTargetAppEntity.handle(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateApp(final TargetAppEntity app, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTargetAppEntity.handle(app);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByPackage(final String packageName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByPackage.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByPackage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object snoozeApp(final String packageName, final long until,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSnoozeApp.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, until);
        _argIndex = 2;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSnoozeApp.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setEnabled(final String packageName, final boolean enabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetEnabled.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, packageName);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetEnabled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TargetAppEntity>> getAllApps() {
    final String _sql = "SELECT * FROM target_apps ORDER BY displayName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"target_apps"}, new Callable<List<TargetAppEntity>>() {
      @Override
      @NonNull
      public List<TargetAppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfBaseDelaySeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDelaySeconds");
          final int _cursorIndexOfInterventionType = CursorUtil.getColumnIndexOrThrow(_cursor, "interventionType");
          final int _cursorIndexOfHardModeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "hardModeEnabled");
          final int _cursorIndexOfSnoozedUntilMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntilMillis");
          final int _cursorIndexOfEscalationPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationPolicy");
          final int _cursorIndexOfReInterventionEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionEnabled");
          final int _cursorIndexOfReInterventionIntervalMin = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionIntervalMin");
          final List<TargetAppEntity> _result = new ArrayList<TargetAppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TargetAppEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final int _tmpBaseDelaySeconds;
            _tmpBaseDelaySeconds = _cursor.getInt(_cursorIndexOfBaseDelaySeconds);
            final String _tmpInterventionType;
            _tmpInterventionType = _cursor.getString(_cursorIndexOfInterventionType);
            final boolean _tmpHardModeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHardModeEnabled);
            _tmpHardModeEnabled = _tmp_1 != 0;
            final long _tmpSnoozedUntilMillis;
            _tmpSnoozedUntilMillis = _cursor.getLong(_cursorIndexOfSnoozedUntilMillis);
            final String _tmpEscalationPolicy;
            _tmpEscalationPolicy = _cursor.getString(_cursorIndexOfEscalationPolicy);
            final boolean _tmpReInterventionEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfReInterventionEnabled);
            _tmpReInterventionEnabled = _tmp_2 != 0;
            final int _tmpReInterventionIntervalMin;
            _tmpReInterventionIntervalMin = _cursor.getInt(_cursorIndexOfReInterventionIntervalMin);
            _item = new TargetAppEntity(_tmpPackageName,_tmpDisplayName,_tmpEnabled,_tmpBaseDelaySeconds,_tmpInterventionType,_tmpHardModeEnabled,_tmpSnoozedUntilMillis,_tmpEscalationPolicy,_tmpReInterventionEnabled,_tmpReInterventionIntervalMin);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TargetAppEntity>> getEnabledApps() {
    final String _sql = "SELECT * FROM target_apps WHERE enabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"target_apps"}, new Callable<List<TargetAppEntity>>() {
      @Override
      @NonNull
      public List<TargetAppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfBaseDelaySeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDelaySeconds");
          final int _cursorIndexOfInterventionType = CursorUtil.getColumnIndexOrThrow(_cursor, "interventionType");
          final int _cursorIndexOfHardModeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "hardModeEnabled");
          final int _cursorIndexOfSnoozedUntilMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntilMillis");
          final int _cursorIndexOfEscalationPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationPolicy");
          final int _cursorIndexOfReInterventionEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionEnabled");
          final int _cursorIndexOfReInterventionIntervalMin = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionIntervalMin");
          final List<TargetAppEntity> _result = new ArrayList<TargetAppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TargetAppEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final int _tmpBaseDelaySeconds;
            _tmpBaseDelaySeconds = _cursor.getInt(_cursorIndexOfBaseDelaySeconds);
            final String _tmpInterventionType;
            _tmpInterventionType = _cursor.getString(_cursorIndexOfInterventionType);
            final boolean _tmpHardModeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHardModeEnabled);
            _tmpHardModeEnabled = _tmp_1 != 0;
            final long _tmpSnoozedUntilMillis;
            _tmpSnoozedUntilMillis = _cursor.getLong(_cursorIndexOfSnoozedUntilMillis);
            final String _tmpEscalationPolicy;
            _tmpEscalationPolicy = _cursor.getString(_cursorIndexOfEscalationPolicy);
            final boolean _tmpReInterventionEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfReInterventionEnabled);
            _tmpReInterventionEnabled = _tmp_2 != 0;
            final int _tmpReInterventionIntervalMin;
            _tmpReInterventionIntervalMin = _cursor.getInt(_cursorIndexOfReInterventionIntervalMin);
            _item = new TargetAppEntity(_tmpPackageName,_tmpDisplayName,_tmpEnabled,_tmpBaseDelaySeconds,_tmpInterventionType,_tmpHardModeEnabled,_tmpSnoozedUntilMillis,_tmpEscalationPolicy,_tmpReInterventionEnabled,_tmpReInterventionIntervalMin);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getEnabledAppsList(final Continuation<? super List<TargetAppEntity>> $completion) {
    final String _sql = "SELECT * FROM target_apps WHERE enabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TargetAppEntity>>() {
      @Override
      @NonNull
      public List<TargetAppEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfBaseDelaySeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDelaySeconds");
          final int _cursorIndexOfInterventionType = CursorUtil.getColumnIndexOrThrow(_cursor, "interventionType");
          final int _cursorIndexOfHardModeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "hardModeEnabled");
          final int _cursorIndexOfSnoozedUntilMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntilMillis");
          final int _cursorIndexOfEscalationPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationPolicy");
          final int _cursorIndexOfReInterventionEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionEnabled");
          final int _cursorIndexOfReInterventionIntervalMin = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionIntervalMin");
          final List<TargetAppEntity> _result = new ArrayList<TargetAppEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TargetAppEntity _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final int _tmpBaseDelaySeconds;
            _tmpBaseDelaySeconds = _cursor.getInt(_cursorIndexOfBaseDelaySeconds);
            final String _tmpInterventionType;
            _tmpInterventionType = _cursor.getString(_cursorIndexOfInterventionType);
            final boolean _tmpHardModeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHardModeEnabled);
            _tmpHardModeEnabled = _tmp_1 != 0;
            final long _tmpSnoozedUntilMillis;
            _tmpSnoozedUntilMillis = _cursor.getLong(_cursorIndexOfSnoozedUntilMillis);
            final String _tmpEscalationPolicy;
            _tmpEscalationPolicy = _cursor.getString(_cursorIndexOfEscalationPolicy);
            final boolean _tmpReInterventionEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfReInterventionEnabled);
            _tmpReInterventionEnabled = _tmp_2 != 0;
            final int _tmpReInterventionIntervalMin;
            _tmpReInterventionIntervalMin = _cursor.getInt(_cursorIndexOfReInterventionIntervalMin);
            _item = new TargetAppEntity(_tmpPackageName,_tmpDisplayName,_tmpEnabled,_tmpBaseDelaySeconds,_tmpInterventionType,_tmpHardModeEnabled,_tmpSnoozedUntilMillis,_tmpEscalationPolicy,_tmpReInterventionEnabled,_tmpReInterventionIntervalMin);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getApp(final String packageName,
      final Continuation<? super TargetAppEntity> $completion) {
    final String _sql = "SELECT * FROM target_apps WHERE packageName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TargetAppEntity>() {
      @Override
      @Nullable
      public TargetAppEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final int _cursorIndexOfBaseDelaySeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "baseDelaySeconds");
          final int _cursorIndexOfInterventionType = CursorUtil.getColumnIndexOrThrow(_cursor, "interventionType");
          final int _cursorIndexOfHardModeEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "hardModeEnabled");
          final int _cursorIndexOfSnoozedUntilMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "snoozedUntilMillis");
          final int _cursorIndexOfEscalationPolicy = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationPolicy");
          final int _cursorIndexOfReInterventionEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionEnabled");
          final int _cursorIndexOfReInterventionIntervalMin = CursorUtil.getColumnIndexOrThrow(_cursor, "reInterventionIntervalMin");
          final TargetAppEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            final int _tmpBaseDelaySeconds;
            _tmpBaseDelaySeconds = _cursor.getInt(_cursorIndexOfBaseDelaySeconds);
            final String _tmpInterventionType;
            _tmpInterventionType = _cursor.getString(_cursorIndexOfInterventionType);
            final boolean _tmpHardModeEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHardModeEnabled);
            _tmpHardModeEnabled = _tmp_1 != 0;
            final long _tmpSnoozedUntilMillis;
            _tmpSnoozedUntilMillis = _cursor.getLong(_cursorIndexOfSnoozedUntilMillis);
            final String _tmpEscalationPolicy;
            _tmpEscalationPolicy = _cursor.getString(_cursorIndexOfEscalationPolicy);
            final boolean _tmpReInterventionEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfReInterventionEnabled);
            _tmpReInterventionEnabled = _tmp_2 != 0;
            final int _tmpReInterventionIntervalMin;
            _tmpReInterventionIntervalMin = _cursor.getInt(_cursorIndexOfReInterventionIntervalMin);
            _result = new TargetAppEntity(_tmpPackageName,_tmpDisplayName,_tmpEnabled,_tmpBaseDelaySeconds,_tmpInterventionType,_tmpHardModeEnabled,_tmpSnoozedUntilMillis,_tmpEscalationPolicy,_tmpReInterventionEnabled,_tmpReInterventionIntervalMin);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object isTracked(final String packageName,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM target_apps WHERE packageName = ? AND enabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
