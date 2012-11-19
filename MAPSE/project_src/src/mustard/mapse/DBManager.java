package mustard.mapse;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DBManager extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;
 
    // Database Name
    private static final String DATABASE_NAME = "mapse";
 
    // Settings table name
    private static final String TABLE_SETTINGS = "settings";
 
    // Settings Table Columns names
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ALARM = "alarm";
    private static final String KEY_CAT = "category";
    private static final String KEY_THRESH = "threshold";
    private static final String KEY_FIRSTRUN = "splash";
    
    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
                + KEY_NAME + " TEXT,"
        		+ KEY_EMAIL + " TEXT,"
                + KEY_ALARM + " TEXT,"
        		+ KEY_CAT + " TEXT,"
        		+ KEY_THRESH + " INTEGER,"
        		+ KEY_FIRSTRUN + " INTEGER" + ")";
        db.execSQL(CREATE_SETTINGS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new settings
    void addSettings(UserSettings set) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, set.getUserName()); //user name
        values.put(KEY_EMAIL, set.getUserEmail()); //user email
        values.put(KEY_ALARM, set.getAlarm()); //user alarm
        values.put(KEY_CAT, set.getCat()); //user category
        values.put(KEY_THRESH, set.getAlarmThreshold()); //user alarm threshold
        values.put(KEY_FIRSTRUN, set.getFirstRun()); //first run?
 
        // Inserting Row
        db.insert(TABLE_SETTINGS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting settings
    UserSettings getSettings() {
        SQLiteDatabase db = this.getReadableDatabase();
        UserSettings set = new UserSettings();
 
        Cursor cursor = db.query(TABLE_SETTINGS, null, null, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
 
	        set.setUserName(cursor.getString(0));
	        set.setUserEmail(cursor.getString(1));
	        set.setAlarm(cursor.getString(2));
	        set.setCat(cursor.getString(3));
	        set.setAlarmThreshold(cursor.getInt(4));
	        set.setFirstRun(cursor.getInt(5));
	        
        }//end if
        
        // return strings
        return set;
    }
 
    // Getting All settings
    public List<UserSettings> getAllSettings() {
        List<UserSettings> setList = new ArrayList<UserSettings>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SETTINGS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	UserSettings set = new UserSettings();
                set.setUserName(cursor.getString(0));
                set.setUserEmail(cursor.getString(1));
                set.setAlarm(cursor.getString(2));
                set.setCat(cursor.getString(3));
                set.setAlarmThreshold(cursor.getInt(4));
                set.setFirstRun(cursor.getInt(5));
                
                setList.add(set);
            } while (cursor.moveToNext());
        }
        return setList;
    }
 
    // Updating settings
    public int updateSettings(UserSettings set) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, set.getUserName());
        values.put(KEY_EMAIL, set.getUserEmail());
        values.put(KEY_ALARM, set.getAlarm());
        values.put(KEY_CAT, set.getCat());
        values.put(KEY_THRESH, set.getAlarmThreshold());
        values.put(KEY_FIRSTRUN, set.getFirstRun());
 
        // updating row
        return db.update(TABLE_SETTINGS, values, null, null);
    }
 
    // Deleting settings
    public void deleteSettings() {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "DROP TABLE " + TABLE_SETTINGS;
        Cursor cursor = db.rawQuery(q, null);
        cursor.close();
    }
 
    // Getting setings count
    public int getSettingsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SETTINGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}