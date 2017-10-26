package name.zeno.android.third.rxjava

import android.os.NetworkOnMainThreadException
import android.util.Log
import com.alibaba.fastjson.JSONException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import name.zeno.android.exception.ZException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 统一处理Exception
 * Create Date: 16/6/3
 *
 * @author 陈治谋 (513500085@qq.com)
 */
open class ZObserver<T> : Observer<T> {

  override fun onError(e: Throwable) {
    val error: ZException = when (e) {
      is NetworkOnMainThreadException -> ZException("UI线程执行网络请求", e).code(ZException.ERR_NETWORK_ON_UI)
      is UnknownHostException -> ZException("无法连接服务器", e).code(ZException.ERR_UNKNOWN_HOST)
      is JSONException -> ZException("数据解析异常", e).code(ZException.ERR_DATA_PARSE)
      is SocketTimeoutException -> ZException("连接超时", e).code(ZException.ERR_TIMEOUT)
      is ConnectException -> ZException("网络连接失败", e).code(ZException.ERR_CONNECT_FAILED)
      is HttpException -> {
        when (e.code()) {
          500 -> ZException("服务器繁忙", e).code(ZException.ERR_SERVICE)
          else -> ZException("服务器繁忙[${e.code()}]", e).code(ZException.ERR_SERVICE)
        }
      }
      is ZException -> e
      else -> ZException(e.message, e).code(ZException.ERR_DEFAULT)
    }
    Log.w(TAG, e.message, e)

    handleError(error)
  }

  open fun handleError(e: ZException) {}

  override fun onSubscribe(d: Disposable) {}

  override fun onNext(value: T) {}

  override fun onComplete() {}

  companion object {
    private val TAG = "ZObserver"

    fun <T> next(next: (T) -> Unit) = object : ZObserver<T>() {
      override fun onNext(value: T) = next(value)
    }
  }
}
