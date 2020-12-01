package com.bizbot.bizbot2.background

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoadDataJobService : JobService() {
    companion object{
        private val TAG = "DataLoadJobService"
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "job service start")
            val synchronizationData = SynchronizationData(baseContext)
            val result = synchronizationData.syncData()
            if(result == 1)
                Log.d(TAG, "onStopJob: 데이터 다운 완료")
            else
                Log.d(TAG, "onStopJob: 데이터 다운 실패")
            jobFinished(p0,false)
        }

        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "job service end")

        return false
    }

}