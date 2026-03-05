package com.genpause.app.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.genpause.app.data.entity.EventEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class EventDao_Impl implements EventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EventEntity> __insertionAdapterOfEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  public EventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEventEntity = new EntityInsertionAdapter<EventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `events` (`id`,`timestamp`,`packageName`,`action`,`delaySeconds`,`mode`,`promptId`,`reasonText`,`emotionId`,`intentionDurationMin`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EventEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTimestamp());
        statement.bindString(3, entity.getPackageName());
        statement.bindString(4, entity.getAction());
        statement.bindLong(5, entity.getDelaySeconds());
        statement.bindString(6, entity.getMode());
        if (entity.getPromptId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPromptId());
        }
        if (entity.getReasonText() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getReasonText());
        }
        if (entity.getEmotionId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getEmotionId());
        }
        if (entity.getIntentionDurationMin() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getIntentionDurationMin());
        }
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM events WHERE timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertEvent(final EventEntity event, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long before, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, before);
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
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EventEntity>> getEventsSince(final long since) {
    final String _sql = "SELECT * FROM events WHERE timestamp >= ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfDelaySeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "delaySeconds");
          final int _cursorIndexOfMode = CursorUtil.getColumnIndexOrThrow(_cursor, "mode");
          final int _cursorIndexOfPromptId = CursorUtil.getColumnIndexOrThrow(_cursor, "promptId");
          final int _cursorIndexOfReasonText = CursorUtil.getColumnIndexOrThrow(_cursor, "reasonText");
          final int _cursorIndexOfEmotionId = CursorUtil.getColumnIndexOrThrow(_cursor, "emotionId");
          final int _cursorIndexOfIntentionDurationMin = CursorUtil.getColumnIndexOrThrow(_cursor, "intentionDurationMin");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpDelaySeconds;
            _tmpDelaySeconds = _cursor.getInt(_cursorIndexOfDelaySeconds);
            final String _tmpMode;
            _tmpMode = _cursor.getString(_cursorIndexOfMode);
            final String _tmpPromptId;
            if (_cursor.isNull(_cursorIndexOfPromptId)) {
              _tmpPromptId = null;
            } else {
              _tmpPromptId = _cursor.getString(_cursorIndexOfPromptId);
            }
            final String _tmpReasonText;
            if (_cursor.isNull(_cursorIndexOfReasonText)) {
              _tmpReasonText = null;
            } else {
              _tmpReasonText = _cursor.getString(_cursorIndexOfReasonText);
            }
            final String _tmpEmotionId;
            if (_cursor.isNull(_cursorIndexOfEmotionId)) {
              _tmpEmotionId = null;
            } else {
              _tmpEmotionId = _cursor.getString(_cursorIndexOfEmotionId);
            }
            final Integer _tmpIntentionDurationMin;
            if (_cursor.isNull(_cursorIndexOfIntentionDurationMin)) {
              _tmpIntentionDurationMin = null;
            } else {
              _tmpIntentionDurationMin = _cursor.getInt(_cursorIndexOfIntentionDurationMin);
            }
            _item = new EventEntity(_tmpId,_tmpTimestamp,_tmpPackageName,_tmpAction,_tmpDelaySeconds,_tmpMode,_tmpPromptId,_tmpReasonText,_tmpEmotionId,_tmpIntentionDurationMin);
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
  public Object getEventsSinceList(final long since,
      final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "SELECT * FROM events WHERE timestamp >= ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfDelaySeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "delaySeconds");
          final int _cursorIndexOfMode = CursorUtil.getColumnIndexOrThrow(_cursor, "mode");
          final int _cursorIndexOfPromptId = CursorUtil.getColumnIndexOrThrow(_cursor, "promptId");
          final int _cursorIndexOfReasonText = CursorUtil.getColumnIndexOrThrow(_cursor, "reasonText");
          final int _cursorIndexOfEmotionId = CursorUtil.getColumnIndexOrThrow(_cursor, "emotionId");
          final int _cursorIndexOfIntentionDurationMin = CursorUtil.getColumnIndexOrThrow(_cursor, "intentionDurationMin");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpDelaySeconds;
            _tmpDelaySeconds = _cursor.getInt(_cursorIndexOfDelaySeconds);
            final String _tmpMode;
            _tmpMode = _cursor.getString(_cursorIndexOfMode);
            final String _tmpPromptId;
            if (_cursor.isNull(_cursorIndexOfPromptId)) {
              _tmpPromptId = null;
            } else {
              _tmpPromptId = _cursor.getString(_cursorIndexOfPromptId);
            }
            final String _tmpReasonText;
            if (_cursor.isNull(_cursorIndexOfReasonText)) {
              _tmpReasonText = null;
            } else {
              _tmpReasonText = _cursor.getString(_cursorIndexOfReasonText);
            }
            final String _tmpEmotionId;
            if (_cursor.isNull(_cursorIndexOfEmotionId)) {
              _tmpEmotionId = null;
            } else {
              _tmpEmotionId = _cursor.getString(_cursorIndexOfEmotionId);
            }
            final Integer _tmpIntentionDurationMin;
            if (_cursor.isNull(_cursorIndexOfIntentionDurationMin)) {
              _tmpIntentionDurationMin = null;
            } else {
              _tmpIntentionDurationMin = _cursor.getInt(_cursorIndexOfIntentionDurationMin);
            }
            _item = new EventEntity(_tmpId,_tmpTimestamp,_tmpPackageName,_tmpAction,_tmpDelaySeconds,_tmpMode,_tmpPromptId,_tmpReasonText,_tmpEmotionId,_tmpIntentionDurationMin);
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
  public Object getEventsForApp(final String packageName, final long since,
      final Continuation<? super List<EventEntity>> $completion) {
    final String _sql = "SELECT * FROM events WHERE packageName = ? AND timestamp >= ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    _argIndex = 2;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<EventEntity>>() {
      @Override
      @NonNull
      public List<EventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfDelaySeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "delaySeconds");
          final int _cursorIndexOfMode = CursorUtil.getColumnIndexOrThrow(_cursor, "mode");
          final int _cursorIndexOfPromptId = CursorUtil.getColumnIndexOrThrow(_cursor, "promptId");
          final int _cursorIndexOfReasonText = CursorUtil.getColumnIndexOrThrow(_cursor, "reasonText");
          final int _cursorIndexOfEmotionId = CursorUtil.getColumnIndexOrThrow(_cursor, "emotionId");
          final int _cursorIndexOfIntentionDurationMin = CursorUtil.getColumnIndexOrThrow(_cursor, "intentionDurationMin");
          final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final String _tmpAction;
            _tmpAction = _cursor.getString(_cursorIndexOfAction);
            final int _tmpDelaySeconds;
            _tmpDelaySeconds = _cursor.getInt(_cursorIndexOfDelaySeconds);
            final String _tmpMode;
            _tmpMode = _cursor.getString(_cursorIndexOfMode);
            final String _tmpPromptId;
            if (_cursor.isNull(_cursorIndexOfPromptId)) {
              _tmpPromptId = null;
            } else {
              _tmpPromptId = _cursor.getString(_cursorIndexOfPromptId);
            }
            final String _tmpReasonText;
            if (_cursor.isNull(_cursorIndexOfReasonText)) {
              _tmpReasonText = null;
            } else {
              _tmpReasonText = _cursor.getString(_cursorIndexOfReasonText);
            }
            final String _tmpEmotionId;
            if (_cursor.isNull(_cursorIndexOfEmotionId)) {
              _tmpEmotionId = null;
            } else {
              _tmpEmotionId = _cursor.getString(_cursorIndexOfEmotionId);
            }
            final Integer _tmpIntentionDurationMin;
            if (_cursor.isNull(_cursorIndexOfIntentionDurationMin)) {
              _tmpIntentionDurationMin = null;
            } else {
              _tmpIntentionDurationMin = _cursor.getInt(_cursorIndexOfIntentionDurationMin);
            }
            _item = new EventEntity(_tmpId,_tmpTimestamp,_tmpPackageName,_tmpAction,_tmpDelaySeconds,_tmpMode,_tmpPromptId,_tmpReasonText,_tmpEmotionId,_tmpIntentionDurationMin);
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
  public Object countByAction(final String action, final long since,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM events WHERE action = ? AND timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, action);
    _argIndex = 2;
    _statement.bindLong(_argIndex, since);
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

  @Override
  public Object countByAppAndAction(final String packageName, final String action, final long since,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM events WHERE packageName = ? AND action = ? AND timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, packageName);
    _argIndex = 2;
    _statement.bindString(_argIndex, action);
    _argIndex = 3;
    _statement.bindLong(_argIndex, since);
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

  @Override
  public Flow<Integer> observeAttemptCount(final long since) {
    final String _sql = "SELECT COUNT(*) FROM events WHERE action = 'ATTEMPT' AND timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> observeCancelCount(final long since) {
    final String _sql = "SELECT COUNT(*) FROM events WHERE action = 'CANCEL' AND timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> observeOpenCount(final long since) {
    final String _sql = "SELECT COUNT(*) FROM events WHERE action = 'OPEN' AND timestamp >= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"events"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTopAttemptedApps(final long since, final int limit,
      final Continuation<? super List<AppAttemptCount>> $completion) {
    final String _sql = "\n"
            + "        SELECT packageName, COUNT(*) as cnt FROM events \n"
            + "        WHERE action = 'ATTEMPT' AND timestamp >= ?\n"
            + "        GROUP BY packageName ORDER BY cnt DESC LIMIT ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AppAttemptCount>>() {
      @Override
      @NonNull
      public List<AppAttemptCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPackageName = 0;
          final int _cursorIndexOfCnt = 1;
          final List<AppAttemptCount> _result = new ArrayList<AppAttemptCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppAttemptCount _item;
            final String _tmpPackageName;
            _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            final int _tmpCnt;
            _tmpCnt = _cursor.getInt(_cursorIndexOfCnt);
            _item = new AppAttemptCount(_tmpPackageName,_tmpCnt);
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
  public Object getHighRiskHours(final long since, final int limit,
      final Continuation<? super List<HourCount>> $completion) {
    final String _sql = "\n"
            + "        SELECT (timestamp / 3600000) % 24 as hour, COUNT(*) as cnt FROM events \n"
            + "        WHERE action = 'ATTEMPT' AND timestamp >= ?\n"
            + "        GROUP BY hour ORDER BY cnt DESC LIMIT ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HourCount>>() {
      @Override
      @NonNull
      public List<HourCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHour = 0;
          final int _cursorIndexOfCnt = 1;
          final List<HourCount> _result = new ArrayList<HourCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HourCount _item;
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpCnt;
            _tmpCnt = _cursor.getInt(_cursorIndexOfCnt);
            _item = new HourCount(_tmpHour,_tmpCnt);
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
  public Object getHourlyAttemptCounts(final long since,
      final Continuation<? super List<HourCount>> $completion) {
    final String _sql = "\n"
            + "        SELECT (timestamp / 3600000) % 24 as hour, COUNT(*) as cnt FROM events \n"
            + "        WHERE action = 'ATTEMPT' AND timestamp >= ?\n"
            + "        GROUP BY hour ORDER BY hour ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, since);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HourCount>>() {
      @Override
      @NonNull
      public List<HourCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHour = 0;
          final int _cursorIndexOfCnt = 1;
          final List<HourCount> _result = new ArrayList<HourCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HourCount _item;
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpCnt;
            _tmpCnt = _cursor.getInt(_cursorIndexOfCnt);
            _item = new HourCount(_tmpHour,_tmpCnt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
