package com.drexask.autocaller.mobile.caller.domain

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.speech.tts.TextToSpeech
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import com.drexask.autocaller.mobile.core.callStatus
import com.drexask.autocaller.mobile.core.utils.CallStatus
import kotlinx.coroutines.delay
import java.lang.reflect.Method

class InformService(private val context: Context) {

    private val telephonyManager = getTelephonyManager()
    private val telecomManager = getTelecomManager()
    private val iTelephonyMethod = getITelephonyMethod()
    private val smsManager = getSmsManager()

    val timeToWaitAnswerSeconds = 20

    suspend fun makeCall(
        textToSpeechService: TextToSpeech,
        phoneNumber: String,
        messageToReadAloud: String
    ): Boolean {

        val toDial = "tel:$phoneNumber"

        val intent = Intent(Intent.ACTION_CALL, Uri.parse(toDial)).apply {
            putExtra(
                TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE,
                true
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)

        callStatus = CallStatus.RINGING

        var waitingTimeSeconds = 0
        while (callStatus == CallStatus.RINGING) {
            delay(1000)
            Log.d("calls", "Звоним... $waitingTimeSeconds/$timeToWaitAnswerSeconds")

            if(waitingTimeSeconds >= timeToWaitAnswerSeconds) {
                Log.d("calls", "Собеседник не ответил на звонок")
                endCall()
                Log.d("calls", "Звонок завершен")
                return false
            }

            if (telephonyManager.callState == TelephonyManager.CALL_STATE_IDLE) {
                Log.d("calls", "Собеседник сбросил звонок")
                textToSpeechService.stop()
                return false
            }
            waitingTimeSeconds++
        }

        val audioManager =
            context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setSpeakerphoneOn(true)

        textToSpeechService.speak(
            messageToReadAloud,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )

        audioManager.isSpeakerphoneOn = true

        while (callStatus == CallStatus.ACCEPTED) {

            Log.d("calls", "Звонок активен")

            while (textToSpeechService.isSpeaking) {
                Log.d("calls", "Текст говорится")

                if (telephonyManager.callState == TelephonyManager.CALL_STATE_IDLE) {
                    Log.d("calls", "Звонок сброшен собеседником")
                    textToSpeechService.stop()
                    return false
                }

                delay(500)
            }

            callStatus = CallStatus.IDLE
        }

        Log.d("calls", "Текст высказан")
        endCall()
        Log.d("calls", "Звонок завершен")

        return true
    }

    fun sendSms(
        phoneNumber: String,
        messageToSend: String
    ) {
        val messageToSendArray = smsManager.divideMessage(messageToSend)
        messageToSendArray.map {
            smsManager.sendTextMessage(phoneNumber, null, it, null, null)
        }
        Log.d("calls", "SMS отправлено")
    }

    private fun endCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            telecomManager?.endCall()
        } else {
            val iTelephony = iTelephonyMethod?.invoke(telephonyManager)
            val endCallMethod = iTelephony?.javaClass?.getDeclaredMethod("endCall")
            endCallMethod?.invoke(iTelephony)
        }
    }

    private fun getSmsManager(): SmsManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }
    }

    private fun getTelephonyManager(): TelephonyManager {
        val telephonyManager: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyManager.registerTelephonyCallback(
                context.mainExecutor,
                object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                    override fun onCallStateChanged(state: Int) {
                        callsListenerCallback(state)
                    }
                })
        } else {
            telephonyManager.listen(object : PhoneStateListener() {
                @Deprecated("Deprecated in Java", ReplaceWith("callsListenerCallback(state)"))
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    callsListenerCallback(state)
                }
            }, PhoneStateListener.LISTEN_CALL_STATE)
        }

        return telephonyManager
    }

    private fun getTelecomManager(): TelecomManager? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val telecomManager =
                context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            telecomManager
        } else null
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun getITelephonyMethod(): Method? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            val iTelephonyMethod = telephonyManager.javaClass.getDeclaredMethod("getITelephony")
            iTelephonyMethod.isAccessible = true
            iTelephonyMethod
        } else null
    }

    private fun callsListenerCallback(state: Int) {
        Log.d("calls", "callsListenerCallback сработал")

        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {}
            TelephonyManager.CALL_STATE_RINGING -> {}
            TelephonyManager.CALL_STATE_OFFHOOK -> {

                Log.d("calls", "Команда разбудить экран сработала")

                val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                val wakeLock =
                    pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AutoCaller:MyWakeLock")
                wakeLock.acquire(1 * 60 * 1000L /*1 minute*/)
            }
        }
    }

}
