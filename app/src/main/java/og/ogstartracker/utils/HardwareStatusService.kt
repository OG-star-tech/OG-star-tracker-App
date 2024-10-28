package og.ogstartracker.utils

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import og.ogstartracker.App.Companion.NOTIFICATION_CHANNEL_ID
import og.ogstartracker.Config
import og.ogstartracker.R
import og.ogstartracker.domain.usecases.arduino.GetCurrentStateUseCase
import og.ogstartracker.network.ErrorIdentification
import og.ogstartracker.ui.MainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HardwareStatusService : Service(), KoinComponent {

	private val getStatus by inject<GetCurrentStateUseCase>()

	private var job: Job? = null

	private var startId: Int? = null

	override fun onCreate() {
		super.onCreate()
		val notification = buildNotification(this, getString(R.string.notifications_init_value))
		startForeground(NOTIFICATION_ID, notification)
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		this.startId = startId
		startRepeatedlyCheckStatus()
		return START_STICKY
	}

	private fun isAppRunning(context: Context): Boolean {
		val activityManager = context.getSystemService<ActivityManager>() ?: return false
		val runningTasks = activityManager.getRunningTasks(1)
		if (runningTasks.isNotEmpty()) {
			val topActivity = runningTasks[0].topActivity
			if (topActivity?.packageName == context.packageName) {
				return true
			}
		}
		return false
	}

	private fun startRepeatedlyCheckStatus() {
		job?.cancel()
		job = CoroutineScope(Dispatchers.Default).launch {
			while (true) {
				val connected = checkWifiConnection(this@HardwareStatusService)
				if (!connected) {
					job?.cancel()
					return@launch
				}

				val mNotificationManager = getSystemService<NotificationManager>() ?: return@launch

				getStatus(GetCurrentStateUseCase.Input(showInUI = false)).onSuccess {
					val notification = buildNotification(this@HardwareStatusService, it ?: handleErrorMessage())
					mNotificationManager.notify(NOTIFICATION_ID, notification)
				}.onError {
					val notification = buildNotification(this@HardwareStatusService, handleErrorMessage(it))
					mNotificationManager.notify(NOTIFICATION_ID, notification)
				}
				delay(REPEAT_DURATION.takeIf { isAppRunning(this@HardwareStatusService) } ?: REPEAT_DURATION_BACKGROUND)
			}
		}
	}

	private fun handleErrorMessage(error: ErrorIdentification? = null): String {
		val status = when (error) {
			// add more options when needed
			else -> "Unknown error"
		}

		return status
	}

	override fun onBind(intent: Intent?): IBinder? {
		return null
	}

	override fun onDestroy() {
		stopForegroundService()
		super.onDestroy()
	}

	/**
	 * Stops this service by canceling the notification (just a fallback), stops foreground with
	 * removal and stops self. Then it stops the coroutine context and marks self as not started.
	 */
	private fun stopForegroundService() {
		val notificationManager = NotificationManagerCompat.from(applicationContext)
		notificationManager.cancel(NOTIFICATION_ID)
		stopForeground(STOP_FOREGROUND_REMOVE)
		startId?.let { stopSelf(it) } ?: kotlin.run { stopSelf() }
	}

	private fun buildNotification(context: Context, subtitle: String): Notification {
		val pendingIntent: PendingIntent = PendingIntent.getActivity(
			context,
			0,
			Intent(context, MainActivity::class.java),
			PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		)

		return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setCategory(NotificationCompat.CATEGORY_PROGRESS)
			.setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
			.setContentTitle(getString(R.string.notifications_title))
			.setContentText(subtitle)
			.setOngoing(true)
			.setContentIntent(pendingIntent)
			.setColor(ContextCompat.getColor(this, R.color.red))
			.setColorized(true)
			.setSmallIcon(R.drawable.ic_notification)
			.build()
	}

	private fun checkWifiConnection(
		context: Context,
	): Boolean {
		val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return true
		val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

		networkCapabilities?.takeIf { it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) }?.let {
			val wifiManager = context.getSystemService<WifiManager>() ?: return false

			val correctWifi = wifiManager.connectionInfo.ssid == Config.WIFI_SSID
				|| wifiManager.connectionInfo.ssid == Config.WIFI_SSID_UNKNOWN
			if (!correctWifi) {
				stopForegroundService()
			}
			return correctWifi
		} ?: run {
			stopForegroundService()
		}

		return false
	}

	companion object {
		private const val NOTIFICATION_ID = 10_001
		private const val REPEAT_DURATION = 5000L
		private const val REPEAT_DURATION_BACKGROUND = 60000L
	}
}