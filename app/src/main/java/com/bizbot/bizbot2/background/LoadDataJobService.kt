package com.bizbot.bizbot2.background

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.util.Log

class LoadDataJobService : JobService() {
    companion object{
        private val TAG = "DataLoadJobService"
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob: ${p0?.jobId}")
        return false
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob: ${p0?.jobId}")
        return false
    }

}