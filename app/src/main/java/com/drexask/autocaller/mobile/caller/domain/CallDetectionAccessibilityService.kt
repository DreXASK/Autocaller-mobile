package com.drexask.autocaller.mobile.caller.domain

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.ServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import com.drexask.autocaller.mobile.core.callStatus
import com.drexask.autocaller.mobile.core.previousChildCount
import com.drexask.autocaller.mobile.core.utils.CallStatus
import kotlin.math.abs

class CallDetectionAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (callStatus == CallStatus.RINGING) {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                Log.d("calls", "in window changed")

                val info = event.source
                Log.d(
                    "calls",
                    "ChildCount ===== ${info?.childCount}, previousChildCount ===== $previousChildCount"
                )

                if (previousChildCount == null) {
                    previousChildCount = info?.childCount ?: 0
                } else {
                    if (abs(info?.childCount?.minus(previousChildCount!!) ?: 0) >= 2) {
                        Log.d("CallDetection.onAccessibilityEvent", "Call answered")
                        callStatus = CallStatus.ACCEPTED
                    }
                }
            } else
                Log.d("calls", "Nothing has changed")
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Toast.makeText(this, "Service connected", Toast.LENGTH_SHORT).show()
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.notificationTimeout = 0
        info.packageNames = null
        setServiceInfo(info)
    }

    override fun onInterrupt() {
    }

    fun isAccessibilityServiceEnabled(): Boolean {
        val am: AccessibilityManager =
            getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices: List<AccessibilityServiceInfo> =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        for (enabledService in enabledServices) {
            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName.equals(packageName) && enabledServiceInfo.name.equals(
                    this.javaClass.name
                )
            ) return true
        }

        return false
    }
}



/*println(" ACTION ===== ${info?.childCount}")
info?.text?.let {
val duration = it
println("Duration = $duration")
val zeroSeconds = String.format("%02d:%02d", *arrayOf<Any>(0, 0))
val firstSecond = String.format("%02d:%02d", *arrayOf<Any>(0, 1))
Log.d(
    "myaccess",
    "after calculation - $zeroSeconds --- $firstSecond --- $duration"
)

if (duration == "Mute") {
    callStatus = CallStatuses.IDLE
    Log.d(
        "myaccess",
        "Call is ended"
    )
}

if (zeroSeconds == duration || firstSecond == duration) {
    Toast.makeText(applicationContext, "Call answered", Toast.LENGTH_SHORT).show()
    callStatus = CallStatuses.ACCEPTED
}
info.recycle()
*/