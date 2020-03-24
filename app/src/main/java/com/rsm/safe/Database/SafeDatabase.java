package com.rsm.safe.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rsm.safe.Bean.TrustedContactModel;

import java.util.ArrayList;
import java.util.List;

public class SafeDatabase extends SQLiteOpenHelper {

    private Context context;

    private static final String DB_NAME = "SAFE";
    private static final String TABLE_NAME = "SAFE_TABLE";
    private static final int DB_VERSION = 2;

    static final String ID = "ID";
    static final String NAME = "NAME";
    static final String NUMBER = "NUMBER";

    public SafeDatabase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SAFE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + NAME + " TEXT NOT NULL, "
                + NUMBER + " INTEGER NOT NULL" + " )";
        sqLiteDatabase.execSQL(CREATE_SAFE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public int addTrusted(TrustedContactModel contact){
        if (getCount() >= 5){
            return 1;
        }
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(NUMBER, contact.getNumber());
        if (database.insert(TABLE_NAME, null, values) == -1){
            database.close();
            return 2;
        }
        database.close();
        return 0;
    }

    public List<TrustedContactModel> getTrustedContacts(){
        List<TrustedContactModel> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                long number = cursor.getLong(2);
                TrustedContactModel contact = new TrustedContactModel(id, name, number);
                list.add(contact);
            }
            while (cursor.moveToNext());
        }
        database.close();
        return list;
    }

    public boolean removeTrusted(int ID){
        SQLiteDatabase database = this.getWritableDatabase();
        if (database.delete(TABLE_NAME, ID + "=?", new String[ID]) == -1){
            return false;
        }
        return true;
    }

    public long getCount(){
        SQLiteDatabase database = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(database, TABLE_NAME);
        database.close();
        return count;
    }
}
