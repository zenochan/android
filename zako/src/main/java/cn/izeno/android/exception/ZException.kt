package cn.izeno.android.exception

import android.os.Build
import cn.izeno.android.util.ZDate
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since  16/6/3
 */
@Suppress("unused")
class ZException : RuntimeException {

  var errCode: Int = 0

  constructor() : super()
  constructor(message: String?) : super(message)
  constructor(message: String?, cause: Throwable?) : super(message, cause)
  constructor(cause: Throwable?) : super(cause)
  constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(message, cause, enableSuppression, writableStackTrace)

  fun code(code: Int) = apply {
    errCode = code
  }

  fun info() = info(this)

  companion object {
    const val ERR_DEFAULT = 1
    const val ERR_SERVICE = 2
    const val ERR_NETWORK_ON_UI = 3
    const val ERR_UNKNOWN_HOST = 4
    const val ERR_DATA_PARSE = 5
    const val ERR_TIMEOUT = 6
    const val ERR_CONNECT_FAILED = 7
    const val ERR_NOT_FOUND = 8

    fun info(throwable: Throwable): String = StringBuilder()
        .appendln("生产厂商：${Build.MANUFACTURER}")
        .appendln("手机型号：${Build.MODEL}")
        .appendln("系统版本：${Build.VERSION.RELEASE}")
        .appendln("异常时间：${ZDate.nowString("yyyy/MM/dd HH:mm:ss")}")
        .appendln("异常类型：${throwable.javaClass.name}")
        .appendln("异常信息：${throwable.message}")
        .appendln("异常堆栈：")
        .append(stack(throwable))
        .toString()

    fun stack(throwable: Throwable): String {
      //构建字符串
      val writer = StringWriter()
      val printWriter = PrintWriter(writer)
      throwable.printStackTrace(printWriter)
      var cause: Throwable? = throwable.cause
      while (cause != null) {
        cause.printStackTrace(printWriter)
        cause = cause.cause
      }

      return writer.toString()
    }
  }

}
