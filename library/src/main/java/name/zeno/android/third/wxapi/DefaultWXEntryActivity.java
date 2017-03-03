package name.zeno.android.third.wxapi;

import android.app.Activity;
import android.content.Intent;

import name.zeno.android.presenter.ZActivity;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/9
 */
@SuppressWarnings("unused")
public abstract class DefaultWXEntryActivity extends ZActivity
{
  /**
   * @return 微信 AppId
   */
  protected abstract String getAppId();

  protected abstract Class<? extends Activity> getIntentClass();

  @Override protected void onNewIntent(Intent intent)
  {
    super.onNewIntent(intent);
    setIntent(intent);
  }

  @Override protected void onResume()
  {
    super.onResume();
    startIntentActivity(getIntent());
  }

  private void startIntentActivity(Intent intent)
  {
    intent.setClass(this, getIntentClass());
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
    finish();
  }

}
