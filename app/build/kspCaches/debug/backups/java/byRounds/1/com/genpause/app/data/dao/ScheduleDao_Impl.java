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
import com.genpause.app.data.entity.ScheduleEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class ScheduleDao_Impl implements ScheduleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScheduleEntity> __insertionAdapterOfScheduleEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduleEntity> __deletionAdapterOfScheduleEntity;

  private final EntityDeletionOrUpdateAdapter<ScheduleEntity> __updateAdapterOfScheduleEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public ScheduleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScheduleEntity = new EntityInsertionAdapter<ScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `schedules` (`id`,`label`,`daysOfWeekMask`,`startMinutes`,`endMinutes`,`strictnessLevel`,`enabled`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getLabel());
        statement.bindLong(3, entity.getDaysOfWeekMask());
        statement.bindLong(4, entity.getStartMinutes());
        statement.bindLong(5, entity.getEndMinutes());
        statement.bindString(6, entity.getStrictnessLevel());
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__deletionAdapterOfScheduleEntity = new EntityDeletionOrUpdateAdapter<ScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `schedules` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfScheduleEntity = new EntityDeletionOrUpdateAdapter<ScheduleEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `schedules` SET `id` = ?,`label` = ?,`daysOfWeekMask` = ?,`startMinutes` = ?,`endMinutes` = ?,`strictnessLevel` = ?,`enabled` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getLabel());
        statement.bindLong(3, entity.getDaysOfWeekMask());
        statement.bindLong(4, entity.getStartMinutes());
        statement.bindLong(5, entity.getEndMinutes());
        statement.bindString(6, entity.getStrictnessLevel());
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM schedules WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSchedule(final ScheduleEntity schedule,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScheduleEntity.insertAndReturnId(schedule);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSchedule(final ScheduleEntity schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfScheduleEntity.handle(schedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSchedule(final ScheduleEntity schedule,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfScheduleEntity.handle(schedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ScheduleEntity>> getAllSchedules() {
    final String _sql = "SELECT * FROM schedules ORDER BY startMinutes ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"schedules"}, new Callable<List<ScheduleEntity>>() {
      @Override
      @NonNull
      public List<ScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfDaysOfWeekMask = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeekMask");
          final int _cursorIndexOfStartMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinutes");
          final int _cursorIndexOfEndMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinutes");
          final int _cursorIndexOfStrictnessLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "strictnessLevel");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final List<ScheduleEntity> _result = new ArrayList<ScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final int _tmpDaysOfWeekMask;
            _tmpDaysOfWeekMask = _cursor.getInt(_cursorIndexOfDaysOfWeekMask);
            final int _tmpStartMinutes;
            _tmpStartMinutes = _cursor.getInt(_cursorIndexOfStartMinutes);
            final int _tmpEndMinutes;
            _tmpEndMinutes = _cursor.getInt(_cursorIndexOfEndMinutes);
            final String _tmpStrictnessLevel;
            _tmpStrictnessLevel = _cursor.getString(_cursorIndexOfStrictnessLevel);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            _item = new ScheduleEntity(_tmpId,_tmpLabel,_tmpDaysOfWeekMask,_tmpStartMinutes,_tmpEndMinutes,_tmpStrictnessLevel,_tmpEnabled);
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
  public Object getEnabledSchedules(final Continuation<? super List<ScheduleEntity>> $completion) {
    final String _sql = "SELECT * FROM schedules WHERE enabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleEntity>>() {
      @Override
      @NonNull
      public List<ScheduleEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfDaysOfWeekMask = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeekMask");
          final int _cursorIndexOfStartMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinutes");
          final int _cursorIndexOfEndMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinutes");
          final int _cursorIndexOfStrictnessLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "strictnessLevel");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final List<ScheduleEntity> _result = new ArrayList<ScheduleEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final int _tmpDaysOfWeekMask;
            _tmpDaysOfWeekMask = _cursor.getInt(_cursorIndexOfDaysOfWeekMask);
            final int _tmpStartMinutes;
            _tmpStartMinutes = _cursor.getInt(_cursorIndexOfStartMinutes);
            final int _tmpEndMinutes;
            _tmpEndMinutes = _cursor.getInt(_cursorIndexOfEndMinutes);
            final String _tmpStrictnessLevel;
            _tmpStrictnessLevel = _cursor.getString(_cursorIndexOfStrictnessLevel);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            _item = new ScheduleEntity(_tmpId,_tmpLabel,_tmpDaysOfWeekMask,_tmpStartMinutes,_tmpEndMinutes,_tmpStrictnessLevel,_tmpEnabled);
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
  public Object getSchedule(final long id, final Continuation<? super ScheduleEntity> $completion) {
    final String _sql = "SELECT * FROM schedules WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduleEntity>() {
      @Override
      @Nullable
      public ScheduleEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfDaysOfWeekMask = CursorUtil.getColumnIndexOrThrow(_cursor, "daysOfWeekMask");
          final int _cursorIndexOfStartMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "startMinutes");
          final int _cursorIndexOfEndMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "endMinutes");
          final int _cursorIndexOfStrictnessLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "strictnessLevel");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final ScheduleEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final int _tmpDaysOfWeekMask;
            _tmpDaysOfWeekMask = _cursor.getInt(_cursorIndexOfDaysOfWeekMask);
            final int _tmpStartMinutes;
            _tmpStartMinutes = _cursor.getInt(_cursorIndexOfStartMinutes);
            final int _tmpEndMinutes;
            _tmpEndMinutes = _cursor.getInt(_cursorIndexOfEndMinutes);
            final String _tmpStrictnessLevel;
            _tmpStrictnessLevel = _cursor.getString(_cursorIndexOfStrictnessLevel);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            _result = new ScheduleEntity(_tmpId,_tmpLabel,_tmpDaysOfWeekMask,_tmpStartMinutes,_tmpEndMinutes,_tmpStrictnessLevel,_tmpEnabled);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
