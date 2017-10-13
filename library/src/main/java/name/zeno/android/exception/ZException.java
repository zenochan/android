package name.zeno.android.exception;

import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import name.zeno.android.util.ZDate;

/**
 * Create Date: 16/6/3
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@SuppressWarnings("unused")
public class ZException extends RuntimeException
{
  public static final int ERR_DEFAULT        = 1;
  public static final int ERR_SERVICE        = 2;
  public static final int ERR_NETWORK_ON_UI  = 3;
  public static final int ERR_UNKNOWN_HOST   = 4;
  public static final int ERR_DATA_PARSE     = 5;
  public static final int ERR_TIMEOUT        = 6;
  public static final int ERR_CONNECT_FAILED = 7;
  public static final int ERR_NOT_FOUND      = 8;

  private int errCode;

  public ZException(int errCode)
  {
    this.errCode = errCode;
  }

  public ZException(int errCode, String detailMessage)
  {
    super(detailMessage);
    this.errCode = errCode;
  }

  public ZException(int errCode, String detailMessage, Throwable throwable)
  {
    super(detailMessage, throwable);
    this.errCode = errCode;
  }

  public ZException(Throwable throwable)
  {
    super(throwable);
  }

  public static String info(Throwable throwable)
  {
    StringBuilder sb;
    sb = new StringBuilder()
        .append("生产厂商：").append(Build.MANUFACTURER).append("\n")
        .append("手机型号：").append(Build.MODEL).append("\n")
        .append("系统版本：").append(Build.VERSION.RELEASE).append("\n")
        .append("异常时间：").append(ZDate.nowString("yyyy/MM/dd HH:mm:ss")).append("\n\n")
        .append("异常类型：").append(throwable.getClass().getName()).append("\n")
        .append("异常信息：").append(throwable.getMessage()).append("\n\n")
        .append("异常堆栈：\n").append(stack(throwable));
    return sb.toString();
  }

  public static String stack(Throwable throwable)
  {
    //构建字符串
    Writer      writer      = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);
    throwable.printStackTrace(printWriter);
    Throwable cause = throwable.getCause();
    while (cause != null) {
      cause.printStackTrace(printWriter);
      cause = cause.getCause();
    }

    return writer.toString();
  }

}
