package name.zeno.android.system;

import android.Manifest;

/**
 * 需要动态申请的权限
 *
 * @author 陈治谋 (513500085@qq.com)
 * @see {@link android.Manifest.permission}
 * @since 2016/11/21.
 */
public interface ZPermission
{
  String READ_EXTERNAL_STORAGE  = "android.permission.READ_EXTERNAL_STORAGE";
  String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

  //  定位
  String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
  String ACCESS_FINE_LOCATION   = Manifest.permission.ACCESS_FINE_LOCATION;
  String READ_PHONE_STATE       = Manifest.permission.READ_PHONE_STATE;

  String CALL_PHONE = Manifest.permission.CALL_PHONE;
}
