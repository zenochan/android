package name.zeno.android.presenter.bigbang;

import android.net.Uri;

import com.alibaba.fastjson.JSON;

import java.net.URLEncoder;
import java.util.List;

import name.zeno.android.data.CommonConnector;
import name.zeno.android.exception.ZException;
import name.zeno.android.listener.Action1;
import name.zeno.android.presenter.BasePresenter;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/4.
 */
class BigBangPresenter extends BasePresenter<BigBangView>
{

  BigBangPresenter(BigBangView view)
  {
    super(view);
  }

  void segments(String text, Action1<List<String>> next)
  {

    CommonConnector.get("http://tomcat.mjtown.cn/zervice/segments/" + Uri.encode(text))
        .map(jsonObject -> {
          SegmentRes res = JSON.parseObject(jsonObject, SegmentRes.class);
          if (res.errCode == 0) {
            return res.data;
          } else {
            throw new ZException(res.errCode, res.errMsg);
          }
        }).subscribe(sub(next));

  }

  public static class SegmentRes
  {
    public int          errCode;
    public String       errMsg;
    public List<String> data;
  }
}
