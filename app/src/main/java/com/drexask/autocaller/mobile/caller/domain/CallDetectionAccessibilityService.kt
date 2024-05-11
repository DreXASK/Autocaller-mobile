package com.drexask.autocaller.mobile.caller.domain

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.drexask.autocaller.mobile.core.callStatus
import com.drexask.autocaller.mobile.core.utils.CallStatus


class CallDetectionAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (callStatus == CallStatus.RINGING) {
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
                val info = event.source

                info?.let {
                    if(info.text?.matches("\\d\\d:\\d\\d".toRegex()) == true) {
                        Toast.makeText(applicationContext, "Call answered", Toast.LENGTH_SHORT)
                            .show()
                        callStatus = CallStatus.ACCEPTED
                    }

                    for (i in 0 until info.childCount) {
                        val child = info.getChild(i)
                        if (child?.text?.matches("\\d\\d:\\d\\d".toRegex()) == true) {
                            Toast.makeText(applicationContext, "Call answered", Toast.LENGTH_SHORT)
                                .show()
                            callStatus = CallStatus.ACCEPTED
                        }
                    }
                }

            }
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
}
