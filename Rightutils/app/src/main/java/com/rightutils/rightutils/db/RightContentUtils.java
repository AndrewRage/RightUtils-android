package com.rightutils.rightutils.db;

import android.content.Context;
import android.database.Cursor;

import com.rightutils.rightutils.collections.RightList;

/**
 * Created by rage on 23.07.15.
 */
public class RightContentUtils extends RightDBUtils {

    @Override
    protected void setDBContext(Context context, String name, int version) {
        super.setDBContext(context, name, version);
    }

    public <T> RightList<T> cursorMapper(Cursor cursor, Class<T> type) {
        return queryListMapper(cursor, type);
    }
}
