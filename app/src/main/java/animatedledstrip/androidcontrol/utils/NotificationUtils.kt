package animatedledstrip.androidcontrol.utils

import androidx.core.app.NotificationManagerCompat

// Notification channel ID
const val channelID = "active_connection"

// Notification ID
const val activeNotificationId = 0

// Notification Manager
lateinit var notificationManager: NotificationManagerCompat

/**
 * Cancel the notification
 */
fun cancelActiveNotification() {
    with(notificationManager) {
        cancel(activeNotificationId)
    }
}
