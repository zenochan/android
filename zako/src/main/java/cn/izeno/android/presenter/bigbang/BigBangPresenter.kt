package cn.izeno.android.presenter.bigbang

import android.net.Uri

import com.alibaba.fastjson.JSON

import cn.izeno.android.data.CommonConnector
import cn.izeno.android.exception.ZException
import cn.izeno.android.presenter.ZPresenter

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/1/4.
 */
internal class BigBangPresenter(view: BigBangView) : ZPresenter<BigBangView>(view) {

  fun segments(text: String, next: (List<String>) -> Unit) {

    CommonConnector.get("http://tomcat.mjtown.cn/zervice/segments/" + Uri.encode(text))
        .map<List<String>> { jsonObject ->
          val res = JSON.parseObject(jsonObject, SegmentRes::class.java)
          if (res.errCode == 0) {
            return@map res.data
          } else {
            throw ZException(res.errMsg).code(res.errCode)
          }
        }.sub(next)
  }

  class SegmentRes {
    var errCode: Int = 0
    var errMsg: String? = null
    var data: List<String>? = null
  }
}
