package cn.izeno.android.data.service

import io.reactivex.Observable
import cn.izeno.android.data.models.Crash
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/21.
 */
interface CommonService {
  /** 下载文件 */
  @GET
  @Streaming
  fun download(@Url fileUrl: String): Call<ResponseBody>

  @GET
  fun get(@Url url: String): Observable<String>

  /** 上传错误到个人服务器 */
  @POST("http://blogapi.mjtown.cn/api/crash")
  fun sendError(@Body crash: Crash): Observable<String>
}
