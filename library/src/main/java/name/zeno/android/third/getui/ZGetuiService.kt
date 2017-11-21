package name.zeno.android.third.getui

import android.app.Service
import android.content.Intent
import android.os.IBinder

import com.igexin.sdk.GTServiceManager

/**
 * 个推 service
 */
abstract class ZGetuiService : Service() {
  override fun onCreate() = GTServiceManager.getInstance().onCreate(this)

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    return GTServiceManager.getInstance().onStartCommand(this, intent, flags, startId)
  }

  override fun onBind(intent: Intent?): IBinder? = GTServiceManager.getInstance().onBind(intent)
  override fun onDestroy() = GTServiceManager.getInstance().onDestroy()
  override fun onLowMemory() = GTServiceManager.getInstance().onLowMemory()
}
