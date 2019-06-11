package cn.izeno.android.data


import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import io.reactivex.Observable
import cn.izeno.android.data.models.Crash
import cn.izeno.android.data.service.CommonService
import cn.izeno.android.third.rxjava.RxUtils
import cn.izeno.android.third.rxjava.ZObserver
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.fj.FastJsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Create Date: 16/5/27
 *
 * @author 陈治谋 (513500085@qq.com)
 */
object CommonConnector {

  private val retrofit: Retrofit
  private val commonService: CommonService

  init {
    val client = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).build()
    retrofit = Retrofit.Builder()
        .baseUrl("http://www.baidu.com")
        .addConverterFactory(FastJsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build()

    commonService = retrofit.create(CommonService::class.java)
  }

  fun download(apkUrl: String): Call<ResponseBody> {
    return commonService.download(apkUrl)
  }

  fun get(url: String): Observable<String> {
    return commonService.get(url).compose(RxUtils.applySchedulers())
  }

  fun sendCrash(throwable: Throwable, accountJson: String? = null) {
    val crash = Crash(throwable)

    if (accountJson != null) {
      try {
        crash.account = JSON.parseObject(accountJson)
      } catch (e: Exception) {
        val jo = JSONObject()
        jo.put("account", accountJson)
        crash.account = jo
      }

    }

    commonService.sendError(crash).compose(RxUtils.applySchedulers()).subscribe(ZObserver())
  }
}
