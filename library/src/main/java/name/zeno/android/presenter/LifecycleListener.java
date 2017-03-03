package name.zeno.android.presenter;

import android.content.Intent;

/**
 * Create Date: 16/6/25
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public interface LifecycleListener
{
  void onCreate();

  void onResume();

  void onActivityResult(int requestCode, int resultCode, Intent data);

  /** fragment */
  void onViewCreated();

  /** fragment */
  void onDestroyView();

  void onDestroy();
}
