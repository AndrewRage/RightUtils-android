package com.rightutils.rightutils.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.rightutils.rightutils.collections.RightList;

import java.util.List;

/**
 * Created by rage on 19.07.15.
 */
public abstract class RightContentProvider extends ContentProvider {
    private static final String TAG = RightContentProvider.class.getSimpleName();

    private String baseContentUri;
    private RightDBUtils rightDBUtils;
    private List<String> uriTableList;
    private UriMatcher uriMatcher;

    public void initProvider(RightDBUtils rightDBUtils, String baseContentUri) {
        this.rightDBUtils = rightDBUtils;
        this.baseContentUri = baseContentUri;
        initUriMatcher();
    }

    private void initUriMatcher() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriTableList = getTablesName();

        for (int i = 0; i < uriTableList.size(); i++) {
            uriMatcher.addURI(baseContentUri, uriTableList.get(i), i);
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
            return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + baseContentUri + "/" + tableName;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String tableName = uriTableList.get(uriMatcher.match(uri));
        if (tableName != null) {
            return rightDBUtils.getDbHandler().getWritableDatabase()
                    .query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String tableName = uriTableList.get(uriMatcher.match(uri));
        Uri resultUri = null;
        if (tableName != null) {
            long rowId = rightDBUtils.getDbHandler().getWritableDatabase()
                    .insert(tableName, null, contentValues);
            if (rowId > 0) {
                resultUri = ContentUris.withAppendedId(
                        Uri.parse("content://" + baseContentUri + "/" + tableName),
                        rowId
                );
            }
        }
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = uriTableList.get(uriMatcher.match(uri));
        if (tableName != null) {
            return rightDBUtils.getDbHandler().getWritableDatabase()
                    .delete(tableName, selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        String tableName = uriTableList.get(uriMatcher.match(uri));
        if (tableName != null) {
            return rightDBUtils.getDbHandler().getWritableDatabase()
                    .update(tableName, contentValues, selection, selectionArgs);
        }
        return 0;
    }
}
