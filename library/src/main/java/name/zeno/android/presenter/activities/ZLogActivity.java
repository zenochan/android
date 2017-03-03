package name.zeno.android.presenter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import name.zeno.android.util.ZLog;

/**
 * 周期日志
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/22
 */
public abstract class ZLogActivity extends AppCompatActivity
{
  protected final String TAG;

  public ZLogActivity()
  {
    TAG = getClass().getSimpleName();
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    ZLog.v(TAG, "onCreate()");
  }

  @Override protected void onResume()
  {
    super.onResume();
    ZLog.v(TAG, "onResume()");
  }

  @Override protected void onNewIntent(Intent intent)
  {
    super.onNewIntent(intent);
    ZLog.v(TAG, "onNewIntent()");
  }

  @Override protected void onPause()
  {
    super.onPause();
    ZLog.v(TAG, "onPause()");
  }

  @Override protected void onSaveInstanceState(Bundle outState)
  {
    super.onSaveInstanceState(outState);
    ZLog.v(TAG, "onSaveInstanceState()");
  }

  @Override protected void onDestroy()
  {
    super.onDestroy();
    ZLog.v(TAG, "onDestroy()");
  }
}
