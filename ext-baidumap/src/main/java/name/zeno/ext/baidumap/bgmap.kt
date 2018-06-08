package name.zeno.ext.baidumap

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import com.baidu.location.BDLocation
import io.reactivex.subjects.Subject
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/7
 */


internal fun Context.printSign() {
  try {
    val info = packageManager.getPackageInfo(this.packageName, PackageManager.GET_SIGNATURES)
    val sign = info.signatures[0].toByteArray()
    val input = ByteArrayInputStream(sign)
    val cf = CertificateFactory.getInstance("X509").generateCertificate(input)
    val md = MessageDigest.getInstance("SHA1")
    val pk = md.digest(cf.encoded)
    val hexString = byte2HexFormatted(pk)

    print(""" Sign Info
     -------------------------- sign info --------------------------
     | PackageName | ${this.packageName}
     | SHA1        | $hexString
     -------------------------- sign info  -------------------------
    """)
  } catch (e: Exception) {
  }
}


//这里是将获取到得编码进行16进制转换
private fun byte2HexFormatted(arr: ByteArray): String {
  val str = StringBuilder(arr.size * 2)
  for (i in arr.indices) {
    var h = Integer.toHexString(arr[i].toInt())
    val l = h.length
    if (l == 1)
      h = "0$h"
    if (l > 2)
      h = h.substring(l - 2, l)
    str.append(h.toUpperCase())
    if (i < arr.size - 1)
      str.append(':')
  }
  return str.toString()
}

fun <T : Fragment> T.requestLocation(): Subject<BDLocation> = ILocation.getInstance(activity).requestLocation()
fun <T : Activity> T.requestLocation(): Subject<BDLocation> = ILocation.getInstance(this).requestLocation()
