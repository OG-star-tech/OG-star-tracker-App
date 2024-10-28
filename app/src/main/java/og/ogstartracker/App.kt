package og.ogstartracker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import og.ogstartracker.di.appModule
import og.ogstartracker.di.networkModule
import og.ogstartracker.di.repositoryModule
import og.ogstartracker.di.useCaseModule
import og.ogstartracker.utils.CustomDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

	override fun onCreate() {
		super.onCreate()

		Timber.plant(CustomDebugTree())

		startKoin {
			androidLogger(Level.ERROR)
			androidContext(this@App)
			modules(
				listOf(
					appModule,
					networkModule,
					useCaseModule,
					repositoryModule
				)
			)
		}

		createNotificationChannel(this)
	}

	private fun createNotificationChannel(context: Context) {
		val channel = NotificationChannel(
			NOTIFICATION_CHANNEL_ID,
			getString(R.string.notifications_channel_name),
			NotificationManager.IMPORTANCE_LOW
		).apply {
			description = getString(R.string.notifications_channel_desc)
		}
		val notificationManager: NotificationManager =
			context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.createNotificationChannel(channel)
	}

	companion object {
		const val NOTIFICATION_CHANNEL_ID = "hardware_status_channel"
	}
}