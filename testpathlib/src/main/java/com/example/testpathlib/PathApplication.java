package com.example.testpathlib;

import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.log.CustomLogger;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by Julien Quiévreux on 23/12/13.
 */
public class PathApplication extends Application
{

    private JobManager jobManager;
    public static Context mContext;
    private static PathApplication instance;


    public PathApplication() {
        instance = this;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext=this;
        configureJobManager();
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(this, configuration);
    }

    public static PathApplication getInstance() {
        return instance;
    }

    public JobManager getJobManager() {
        return jobManager;
    }
}
