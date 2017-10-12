package name.zeno.android.presenter.common;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/7.
 */
public class Valid
{
  private boolean valid;
  private String  info;

  public Valid() {}

  public boolean isValid()
  {return this.valid;}

  public String getInfo()
  {return this.info;}

  public void setValid(boolean valid)
  {this.valid = valid; }

  public void setInfo(String info)
  {this.info = info; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof Valid)) return false;
    final Valid other = (Valid) o;
    if (!other.canEqual((Object) this)) return false;
    if (this.isValid() != other.isValid()) return false;
    final Object this$info  = this.getInfo();
    final Object other$info = other.getInfo();
    if (this$info == null ? other$info != null : !this$info.equals(other$info)) return false;
    return true;
  }

  public int hashCode()
  {
    final int PRIME  = 59;
    int       result = 1;
    result = result * PRIME + (this.isValid() ? 79 : 97);
    final Object $info = this.getInfo();
    result = result * PRIME + ($info == null ? 43 : $info.hashCode());
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof Valid;}

  public String toString()
  {return "Valid(valid=" + this.isValid() + ", info=" + this.getInfo() + ")";}
}
