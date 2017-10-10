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

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public int getType()
  {
    return type;
  }

  public void setType(int type)
  {
    this.type = type;
  }

  public String getResult()
  {
    return result;
  }

  public void setResult(String result)
  {
    this.result = result;
  }

  public String getPreFill()
  {
    return preFill;
  }

  public void setPreFill(String preFill)
  {
    this.preFill = preFill;
  }

  public String getHint()
  {
    return hint;
  }

  public void setHint(String hint)
  {
    this.hint = hint;
  }

  public String getRegex()
  {
    return regex;
  }

  public void setRegex(String regex)
  {
    this.regex = regex;
  }

  public String getRegexHint()
  {
    return regexHint;
  }

  public void setRegexHint(String regexHint)
  {
    this.regexHint = regexHint;
  }

  public boolean isWhiteStatusBar()
  {
    return whiteStatusBar;
  }

  public void setWhiteStatusBar(boolean whiteStatusBar)
  {
    this.whiteStatusBar = whiteStatusBar;
  }

  public int getBtnBackground()
  {
    return btnBackground;
  }

  public void setBtnBackground(int btnBackground)
  {
    this.btnBackground = btnBackground;
  }

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
