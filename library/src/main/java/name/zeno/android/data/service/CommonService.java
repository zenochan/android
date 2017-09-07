package name.zeno.android.data.service;

import io.reactivex.Observable;
import name.zeno.android.data.models.Crash;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/21.
 */
public interface CommonService
{
  @Streaming
  @GET() Call<ResponseBody> download(@Url String fileUrl);

  @GET() Observable<String> get(@Url String url);

  /**
   * 上传错误到个人服务器
   */
  @POST("http://blogapi.mjtown.cn/api/crash")
  Observable<String> sendError(@Body Crash crash);
}
