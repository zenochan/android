package name.zeno.android.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class TextData implements Parcelable
{
  public static final int TYPE_TEXT    = 0;//单行文本
  public static final int TYPE_NUMBER  = 1;//数字
  public static final int TYPE_PHONE   = 2;//手机号
  public static final int TYPE_CONTENT = 3;//多行文本

  public String getTitle()
  {return this.title;}

  public int getType()
  {return this.type;}

  public String getResult()
  {return this.result;}

  public String getPreFill()
  {return this.preFill;}

  public String getHint()
  {return this.hint;}

  public String getRegex()
  {return this.regex;}

  public String getRegexHint()
  {return this.regexHint;}

  public boolean isWhiteStatusBar()
  {return this.whiteStatusBar;}

  public int getBtnBackground()
  {return this.btnBackground;}

  public void setTitle(String title)
  {this.title = title; }

  public void setResult(String result)
  {this.result = result; }

  public void setPreFill(String preFill)
  {this.preFill = preFill; }

  public void setHint(String hint)
  {this.hint = hint; }

  public void setRegex(String regex)
  {this.regex = regex; }

  public void setRegexHint(String regexHint)
  {this.regexHint = regexHint; }

  public void setWhiteStatusBar(boolean whiteStatusBar)
  {this.whiteStatusBar = whiteStatusBar; }

  public void setBtnBackground(int btnBackground)
  {this.btnBackground = btnBackground; }

  public boolean equals(Object o)
  {
    if (o == this) return true;
    if (!(o instanceof TextData)) return false;
    final TextData other = (TextData) o;
    if (!other.canEqual((Object) this)) return false;
    final Object this$title  = this.getTitle();
    final Object other$title = other.getTitle();
    if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
    if (this.getType() != other.getType()) return false;
    final Object this$result  = this.getResult();
    final Object other$result = other.getResult();
    if (this$result == null ? other$result != null : !this$result.equals(other$result))
      return false;
    final Object this$preFill  = this.getPreFill();
    final Object other$preFill = other.getPreFill();
    if (this$preFill == null ? other$preFill != null : !this$preFill.equals(other$preFill))
      return false;
    final Object this$hint  = this.getHint();
    final Object other$hint = other.getHint();
    if (this$hint == null ? other$hint != null : !this$hint.equals(other$hint)) return false;
    final Object this$regex  = this.getRegex();
    final Object other$regex = other.getRegex();
    if (this$regex == null ? other$regex != null : !this$regex.equals(other$regex)) return false;
    final Object this$regexHint  = this.getRegexHint();
    final Object other$regexHint = other.getRegexHint();
    if (this$regexHint == null ? other$regexHint != null : !this$regexHint.equals(other$regexHint))
      return false;
    if (this.isWhiteStatusBar() != other.isWhiteStatusBar()) return false;
    if (this.getBtnBackground() != other.getBtnBackground()) return false;
    return true;
  }

  public int hashCode()
  {
    final int    PRIME  = 59;
    int          result = 1;
    final Object $title = this.getTitle();
    result = result * PRIME + ($title == null ? 43 : $title.hashCode());
    result = result * PRIME + this.getType();
    final Object $result = this.getResult();
    result = result * PRIME + ($result == null ? 43 : $result.hashCode());
    final Object $preFill = this.getPreFill();
    result = result * PRIME + ($preFill == null ? 43 : $preFill.hashCode());
    final Object $hint = this.getHint();
    result = result * PRIME + ($hint == null ? 43 : $hint.hashCode());
    final Object $regex = this.getRegex();
    result = result * PRIME + ($regex == null ? 43 : $regex.hashCode());
    final Object $regexHint = this.getRegexHint();
    result = result * PRIME + ($regexHint == null ? 43 : $regexHint.hashCode());
    result = result * PRIME + (this.isWhiteStatusBar() ? 79 : 97);
    result = result * PRIME + this.getBtnBackground();
    return result;
  }

  protected boolean canEqual(Object other)
  {return other instanceof TextData;}

  public String toString()
  {return "TextData(title=" + this.getTitle() + ", type=" + this.getType() + ", result=" + this.getResult() + ", preFill=" + this.getPreFill() + ", hint=" + this.getHint() + ", regex=" + this.getRegex() + ", regexHint=" + this.getRegexHint() + ", whiteStatusBar=" + this.isWhiteStatusBar() + ", btnBackground=" + this.getBtnBackground() + ")";}

  public void setType(@Type int type)
  {this.type = type; }

  @IntDef({TYPE_TEXT, TYPE_NUMBER, TYPE_PHONE, TYPE_CONTENT}) @interface Type {}

  private String title;
  @Type
  private int    type;
  private String result;
  private String preFill;
  private String hint;

  private String regex;
  private String regexHint;

  private boolean whiteStatusBar;
  @DrawableRes
  private int     btnBackground;

  public static TextData text(String title, String hint, String preFill)
  {
    TextData data = new TextData();
    data.type = TYPE_TEXT;
    data.title = title;
    data.hint = hint;
    data.preFill = preFill;

    return data;
  }

  public TextData() {}


  @Override public int describeContents() { return 0; }

  @Override public void writeToParcel(Parcel dest, int flags)
  {
    dest.writeString(this.title);
    dest.writeInt(this.type);
    dest.writeString(this.result);
    dest.writeString(this.preFill);
    dest.writeString(this.hint);
    dest.writeString(this.regex);
    dest.writeString(this.regexHint);
    dest.writeByte(this.whiteStatusBar ? (byte) 1 : (byte) 0);
    dest.writeInt(this.btnBackground);
  }

  protected TextData(Parcel in)
  {
    this.title = in.readString();
    this.type = in.readInt();
    this.result = in.readString();
    this.preFill = in.readString();
    this.hint = in.readString();
    this.regex = in.readString();
    this.regexHint = in.readString();
    this.whiteStatusBar = in.readByte() != 0;
    this.btnBackground = in.readInt();
  }

  public static final Creator<TextData> CREATOR = new Creator<TextData>()
  {
    @Override public TextData createFromParcel(Parcel source) {return new TextData(source);}

    @Override public TextData[] newArray(int size) {return new TextData[size];}
  };
}
