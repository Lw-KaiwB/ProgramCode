package com.kb.criminalitent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.kb.criminalitent.database.CrimeBaseHelper;
import com.kb.criminalitent.database.CrimeCursorWrapper;
import com.kb.criminalitent.database.CrimeDbSchema;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab getCrimeLab(Context mContext) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(mContext);
        }
        return sCrimeLab;
    }

    public List<Crime> getCrimes() {
        List<Crime> mCrimes = new ArrayList<>();
        CrimeCursorWrapper mCursor = getCursor(null, null);
        try {
            mCursor.moveToFirst();
            while (!mCursor.isAfterLast()) {
                mCrimes.add(mCursor.getCrime());
                mCursor.moveToNext();
            }
        } finally {
            mCursor.close();
        }
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = getCursor(CrimeDbSchema.CrimeTable.Cols.UUID + " =?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public void addCrime(Crime mCrime) {
        ContentValues values = getContentValues(mCrime);
        mDatabase.insert(CrimeDbSchema.CrimeTable.TABLE_NAME, null, values);
    }

    public void updateCrime(Crime crime) {
        String uuid = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDbSchema.CrimeTable.TABLE_NAME,
                values,
                CrimeDbSchema.CrimeTable.Cols.UUID + "=?",
                new String[]{uuid});
    }

    public void deleteCrime(UUID uuid) {
        mDatabase.delete(CrimeDbSchema.CrimeTable.TABLE_NAME, CrimeDbSchema.CrimeTable.Cols.UUID + " =?", new String[]{uuid.toString()});
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

    private CrimeCursorWrapper getCursor(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(CrimeDbSchema.CrimeTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }
}
