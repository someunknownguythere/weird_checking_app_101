package com.example.mainproject

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context

fun isAppRunning(context: Context) : String? {
    val usageStatsManager = context
        .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val ts = System.currentTimeMillis()
    val queryUsageStats = usageStatsManager
        .queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            ts - 999999999999999999, ts
        )
    if (queryUsageStats == null || queryUsageStats.isEmpty()) {
        return null
    }
    var recentStats: UsageStats? = null
    for (usageStats in queryUsageStats) {
        if (recentStats == null
            || recentStats.lastTimeUsed < usageStats
                .lastTimeUsed
        ) {
            recentStats = usageStats
        }
    }
    return recentStats!!.packageName
}