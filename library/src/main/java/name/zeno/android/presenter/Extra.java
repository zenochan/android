package name.zeno.android.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/9.
 */
public class Extra
{
  public static final String KEY = "intent_data";

  public static Intent setData(Parcelable data)
  {
    return setData(new Intent(), data);
  }

  public static Intent setData(Intent intent, Parcelable data)
  {
    intent.putExtra(KEY, data);
    return intent;
  }

  public static Bundle setData(Bundle bundle, Parcelable data)
  {
    bundle.putParcelable(KEY, data);
    return bundle;
  }

  public static <T extends Parcelable> T getData(Intent intent)
  {
    return intent == null ? null : intent.getParcelableExtra(KEY);
  }

  public static <T extends Parcelable> T getData(Bundle bundle)
  {
    return bundle == null ? null : bundle.getParcelable(KEY);
  }
}
