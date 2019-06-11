package cn.izeno.android.sensor

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import cn.izeno.android.core.navigator
import cn.izeno.android.core.now
import cn.izeno.android.presenter.LifecycleListener
import cn.izeno.android.util.max
import org.jetbrains.anko.sensorManager

/**
 * # 摇一摇辅助类
 *
 * - [Sensor.TYPE_ACCELEROMETER_UNCALIBRATED] 线性加速度传感器
 * - [Sensor.TYPE_ACCELEROMETER] 加速度传感器
 *    > 加速度传感器 = 重力 + 线性加速度传感器
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/16
 */
@Suppress("unused")
class Shake private constructor(
    val context: Context
) : LifecycleListener, SensorEventListener {

  /** 两个摇一摇的间隔 */
  var interval = 1000
  /** 摇一摇灵敏度，值越小越灵敏 */
  var sensibility = 3


  private var lastA = 0         // 最后一次记录的加速度
  private var lastTime = 0L     // 最后记录加速度的时间
  private var step = 4          // 加速度步长

  private var onShake: (() -> Unit)? = null

  private val vas = ArrayList<Int>()    // 加速度记录
  private var lastShake = 0L            // 最后摇一摇的时间

  fun onShake(action: (() -> Unit)?) {
    this.onShake = action
  }

  override fun onResume() {
    super.onResume()
    // 线性加速度传感器
    val sensor = context.sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    if (sensor != null) {
      context.sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    }
  }

  override fun onPause() {
    super.onPause()
    context.sensorManager.unregisterListener(this)
  }

  override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

  override fun onSensorChanged(event: SensorEvent) {
    if (event.sensor.type != Sensor.TYPE_LINEAR_ACCELERATION) return
    // 禁止时间过长，清除记录
    if (now - lastTime > 200) vas.clear()

    //获取三个方向值
    val x = event.values[0]
    val y = event.values[1]
    val z = event.values[2]
//    var a = Math.sqrt(0.0 + x * x + y * y + z * z).toInt()
//    if (x < 0) a = -a
    val a = x.toInt()

    if (Math.abs(this.lastA - a) > step) {
      lastTime = now
      this.lastA = a
      vas.add(a)
      shake()
    }
  }

  private fun shake() {
    // 两个摇一摇的间隔太近, 不判断
    if (now - lastShake < interval) return

    // 加速度改变方向的次数
    var reverse = 0
    var last = vas[0]
    vas.forEach {
      if (it * last < 0) reverse++
      last = it
    }

    if (reverse > max(3, sensibility)) {
      lastShake = now
      vas.clear()
      onShake?.invoke()
    }
  }

  companion object {
    @JvmStatic
    fun with(activity: Activity): Shake {
      val shake = Shake(activity)
      activity.navigator().registerLifecycleListener(shake)
      return shake
    }

    @JvmStatic
    fun with(fragment: Fragment): Shake {
      val shake = Shake(fragment.activity)
      fragment.navigator().registerLifecycleListener(shake)
      return shake
    }
  }
}