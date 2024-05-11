package com.drexask.autocaller.mobile

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.drexask.autocaller.mobile.connectionAdjuster.presentation.ConnectionAdjusterScreen
import com.drexask.autocaller.mobile.caller.domain.CallDetectionAccessibilityService
import com.drexask.autocaller.mobile.caller.presentation.CallerScreen
import com.drexask.autocaller.mobile.core.domain.ServerConnection
import com.drexask.autocaller.mobile.core.domain.usecase.serverConnectionSettings.GetServerConnectionSettingsUseCase
import com.drexask.autocaller.mobile.ui.theme.AutocallerMobileTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import com.drexask.autocaller.mobile.core.domain.utils.Result
import com.drexask.autocaller.mobile.core.domain.utils.ServerConnectionStatus

class MainActivity : ComponentActivity() {

    private val serverConnection by inject<ServerConnection>()

    @SuppressLint("DiscouragedPrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isAccessibilityServiceEnabled(CallDetectionAccessibilityService::class.java)) {
            Toast.makeText(this, "Включите службу Call Detection", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        } else {
            Log.d("calls", "Service is enabled")
        }

        requestPermissions(
            arrayOf(
                Manifest.permission.SEND_SMS,
                Manifest.permission.ANSWER_PHONE_CALLS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG
            ), 100
        )


        initConnection()

        setContent {
            AutocallerMobileTheme(darkTheme = isSystemInDarkTheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {

                    when(serverConnection.connectionStatus.value) {
                        ServerConnectionStatus.DISCONNECTED -> ConnectionAdjusterScreen()
                        ServerConnectionStatus.CONNECTING -> ConnectionAdjusterScreen()
                        ServerConnectionStatus.CONNECTED -> CallerScreen()
                    }
                }
            }
        }
    }


    private fun isAccessibilityServiceEnabled(
        service: Class<out AccessibilityService?>
    ): Boolean {
        val am: AccessibilityManager =
            getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices: List<AccessibilityServiceInfo> =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

        for (enabledService in enabledServices) {
            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName.equals(packageName) && enabledServiceInfo.name.equals(
                    service.name
                )
            ) return true
        }

        return false
    }

    private fun initConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            val getServerConnectionSettingsUseCase by inject<GetServerConnectionSettingsUseCase>()

            when(val result = getServerConnectionSettingsUseCase.execute()) {
                is Result.Success -> serverConnection.loginOnServer(result.data.ip, result.data.port, result.data.token)
                is Result.Error -> println("initConnection failed, error = ${result.error}")
            }
        }
    }
}
