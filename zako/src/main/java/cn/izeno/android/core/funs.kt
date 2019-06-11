package cn.izeno.android.core

import android.util.Log
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * 在 IO 线程中初始化模块
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/28
 */
fun initInIOThread(name: String, block: () -> Unit) {
  Single.create<Long> {
    val start = now
    block()
    it.onSuccess(now - start)
  }.subscribeOn(Schedulers.io()).subscribe { t ->
    Log.e(name, "initialized in $t ms")
  }
}