package name.zeno.android.presenter.bigbang

import android.net.Uri

import com.alibaba.fastjson.JSON

import name.zeno.android.data.CommonConnector
import name.zeno.android.exception.ZException
import name.zeno.android.presenter.BasePresenter

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/4.
 */
internal class BigBangPresenter(view: BigBangView) : BasePresenter<BigBangView>(view) {

  fun segments(text: String, next: (List<String>) -> Unit) {

    CommonConnector.get("http://tomcat.mjtown.cn/zervice/segments/" + Uri.encode(text))
        .map<List<String>> { jsonObject ->
          val res = JSON.parseObject(jsonObject, SegmentRes::class.java)
          if (res.errCode == 0) {
            return@map res.data
          } else {
            throw ZException(res.errMsg).code(res.errCode)
          }
        }.subscribe(sub(next))
  }

  class SegmentRes {
    var errCode: Int = 0
    var errMsg: String? = null
    var data: List<String>? = null
  }
}