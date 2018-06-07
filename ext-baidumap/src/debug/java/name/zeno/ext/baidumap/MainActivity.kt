package name.zeno.ext.baidumap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import name.zeno.ktrxpermission.ZPermission
import name.zeno.ktrxpermission.rxPermissions

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/6
 */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val btn = Button(this)
    btn.text = "获取位置"
    btn.setOnClickListener { getLocation() }
    setContentView(btn)
  }


  fun getLocation() {
    rxPermissions(ZPermission.ACCESS_COARSE_LOCATION, ZPermission.ACCESS_FINE_LOCATION)
        .subscribe({ granted ->
          if (granted) {
            ILocation.getInstance(this).requestLocation().subscribe({ location ->
              Toast.makeText(this, "定位成功", Toast.LENGTH_SHORT).show()
            }, {
              Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            })
          }
        })
  }

}