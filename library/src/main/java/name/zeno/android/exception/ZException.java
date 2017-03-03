package name.zeno.android.exception;

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

  private String message;
  private int    errCode;

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
    this.message = detailMessage;
  }

  public ZException(Throwable throwable)
  {
    super(throwable);
  }
}
