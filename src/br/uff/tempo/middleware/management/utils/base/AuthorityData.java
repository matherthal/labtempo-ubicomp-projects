package br.uff.tempo.middleware.management.utils.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AuthorityData {

  private static final String TAG = AuthorityData.class.getSimpleName();

  private static final int VERSION = 1;
  private static final String DATABASE = "credentials.db";
  private static final String TABLE = "credentials";

  public static final String C_ID = "_id";
  public static final String C_USER = "user";
  public static final String C_DOMAIN = "domain";
  
  private static final String[] DB_DOMAIN_COLUMNS = { C_DOMAIN };

  private static final String GET_ALL_ORDER_BY = C_USER + " DESC";

  private static final String[] MAX_USER_COLUMNS = { "max("
      + AuthorityData.C_USER + ")" };


  private class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
      super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      Log.i(TAG, "Creating database: " + DATABASE);
      db.execSQL("create table " + TABLE + " (" + C_ID + " int primary key, "
          + C_USER + " text, " +  C_DOMAIN + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("drop table " + TABLE);
      this.onCreate(db);
    }
  }

  private final DbHelper dbHelper;

  public AuthorityData(Context context) {
    this.dbHelper = new DbHelper(context);
    Log.i(TAG, "Initialized data");
  }

  public void close() {
    this.dbHelper.close();
  }

  public void insertOrIgnore(ContentValues values) {
    Log.d(TAG, "insertOrIgnore on " + values);
    SQLiteDatabase db = this.dbHelper.getWritableDatabase();
    try {
      db.insertWithOnConflict(TABLE, null, values,
          SQLiteDatabase.CONFLICT_IGNORE);
    } finally {
      db.close();
    }
  }

  /**
   * 
   * @return Cursor where the columns are going to be id, created_at, user, txt
   */
  public Cursor getStatusUpdates() {
    SQLiteDatabase db = this.dbHelper.getReadableDatabase();
    return db.query(TABLE, null, null, null, null, null, GET_ALL_ORDER_BY);
  }

  public long getLatestStatusCreatedAtTime() {
    SQLiteDatabase db = this.dbHelper.getReadableDatabase();
    try {
      Cursor cursor = db.query(TABLE, MAX_USER_COLUMNS, null, null, null,
          null, null);
      try {
        return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
      } finally {
        cursor.close();
      }
    } finally {
      db.close();
    }
  }

  public String getDomainByUser(String user) {
    SQLiteDatabase db = this.dbHelper.getReadableDatabase();
    try {
      Cursor cursor = db.query(TABLE, DB_DOMAIN_COLUMNS, C_USER + "=" + user, null,
          null, null, null);
      try {
        return cursor.moveToNext() ? cursor.getString(0) : null;
      } finally {
        cursor.close();
      }
    } finally {
      db.close();
    }
  }
  
  /**
   * Deletes ALL the data
   */
  public void delete() {
    // Open Database
    SQLiteDatabase db = dbHelper.getWritableDatabase();

    // Delete the data
    db.delete(TABLE, null, null);

    // Close Database
    db.close();
  }

}
