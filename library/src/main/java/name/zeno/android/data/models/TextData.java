package name.zeno.android.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import lombok.Data;
import lombok.Setter;

/**
 * Create Date: 16/6/16
 *
 * @author 陈治谋 (513500085@qq.com)
 */
@Data
public class TextData implements Parcelable
{
  public static final int TYPE_TEXT    = 0;//单行文本
  public static final int TYPE_NUMBER  = 1;//数字
  public static final int TYPE_PHONE   = 2;//手机号
  public static final int TYPE_CONTENT = 3;//多行文本

  @IntDef({TYPE_TEXT, TYPE_NUMBER, TYPE_PHONE, TYPE_CONTENT}) @interface Type {}

  private String title;
  @Type
  @Setter(onParam = @__({@Type}))
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
