package com.rightutils.rightutils.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.rightutils.rightutils.collections.RightList;

import java.util.List;

/**
 * Created by rage on 19.07.15.
 */
public abstract class RightContentProvider extends ContentProvider {
    private static final String TAG = RightContentProvider.class.getSimpleName();

    private static String mBaseContentUri;

    private RightDBUtils rightDBUtils;
    private List<String> uriTableList;
    private UriMatcher uriMatcher;

    public static Uri getUri(Class<?> classType) {
        return Uri.parse("content://" + mBaseContentUri + "/" +classType.getSimpleName().toLowerCase());
    }

    public void initProvider(RightDBUtils rightDBUtils, String baseContentUri) {
        this.rightDBUtils = rightDBUtils;
        mBaseContentUri = baseContentUri;
        initUriMatcher();
    }

    private void initUriMatcher() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriTableList = getTablesName();
        for (int i = 0; i < uriTableList.size(); i++) {
            Log.d(TAG, "URI: " + mBaseContentUri + "/" + uriTableList.get(i));
            uriMatcher.addURI(mBaseContentUri, uriTableList.get(i).toLowerCase(), i);
        }
    }

    private RightList<String> getTablesName() {
        SQLiteDatabase db = rightDBUtils.getDbHandler().getReadableDatabase();
        RightList<String> tablesNames = new RightList<>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                tablesNames.add(c.getString(c.getColumnIndex("name")));
                c.moveToNext();
            }
        }
        return tablesNames;
    }

    @Override
    public String getType(Uri uri) {
        String tableName = uriTableList.get(uriMatcher.match(uri));
        if (tableName != null) {
            return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + mBaseContentUri + "/" + tableName;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);
        if (match >= 0) {
            String tableName = uriTableList.get(match);
            if (tableName != null) {
                return rightDBUtils.getDbHandler().getWritableDatabase()
                        .query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
            }
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        Uri resultUri = null;
        if (match >= 0) {
            String tableName = uriTableList.get(match);
            if (tableName != null) {
                long rowId = rightDBUtils.getDbHandler().getWritableDatabase()
                        .insert(tableName, null, contentValues);
                if (rowId > 0) {
                    resultUri = ContentUris.withAppendedId(
                            Uri.parse("content://" + mBaseContentUri + "/" + tableName),
                            rowId
                    );
                }
            }
        }
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        if (match >= 0) {
            String tableName = uriTableList.get(match);
            if (tableName != null) {
                return rightDBUtils.getDbHandler().getWritableDatabase()
                        .delete(tableName, selection, selectionArgs);
            }
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        if (match >= 0) {
            String tableName = uriTableList.get(match);
            if (tableName != null) {
                return rightDBUtils.getDbHandler().getWritableDatabase()
                        .update(tableName, contentValues, selection, selectionArgs);
            }
        }
        return 0;
    }
}
