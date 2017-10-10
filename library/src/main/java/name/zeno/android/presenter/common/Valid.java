package name.zeno.android.presenter.common;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/7.
 */
public class Valid
{
  private boolean valid;
  private String  info;

  public boolean isValid()
  {
    return valid;
  }

  public void setValid(boolean valid)
  {
    this.valid = valid;
  }

  public String getInfo()
  {
    return info;
  }

  public void setInfo(String info)
  {
    this.info = info;
  }
}
