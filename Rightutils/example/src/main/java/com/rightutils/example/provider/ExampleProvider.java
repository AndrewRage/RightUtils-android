package com.rightutils.example.provider;

import com.rightutils.example.BuildConfig;
import com.rightutils.example.db.DBUtils;
import com.rightutils.rightutils.db.RightContentProvider;

/**
 * Created by rage on 20.07.15.
 */
public class ExampleProvider extends RightContentProvider {
    @Override
    public boolean onCreate() {
        initProvider(
                DBUtils.getInstance(getContext()),
                BuildConfig.APPLICATION_ID + ".provider"
        );
        return true;
    }
}
