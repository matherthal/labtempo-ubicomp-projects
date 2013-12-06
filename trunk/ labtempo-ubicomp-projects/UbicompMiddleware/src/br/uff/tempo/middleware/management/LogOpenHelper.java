package br.uff.tempo.middleware.management;

import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LogOpenHelper extends SQLiteOpenHelper {
	private static final String TAG = "LogOpenHelper";
	
    private static final int DATABASE_VERSION = 2;
    public static final String LOG_TABLE_NAME = "LOG";
    public static final String LOG_COL_DT = "DATETIME";
    public static final String LOG_COL_ACTION = "ACTION";
    public static final String LOG_COL_AUTOR = "ACTOR";
    public static final String LOG_COL_AUTOR_ID = "ACTOR_ID";
    
    private static final String LOG_TABLE_CREATE =
                "CREATE TABLE " + LOG_TABLE_NAME + " (" +
                "_ID INTEGER PRIMARY KEY AUTOINCREMENT" +
                LOG_COL_DT + " INTEGER, " +
                LOG_COL_ACTION + " TEXT, " +
                LOG_COL_AUTOR_ID + " TEXT, " +
                LOG_COL_AUTOR + " TEXT);";

    LogOpenHelper(Context context) {
        super(context, LOG_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOG_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS titles");
        onCreate(db);
    }
    
    public class LogObject {
    	public String AutorName = "";
    	public String AutorRANS = "";
    	public String Action = "";
    	public Date datetime;
    	
    	LogObject() {
    	}
    }
}
