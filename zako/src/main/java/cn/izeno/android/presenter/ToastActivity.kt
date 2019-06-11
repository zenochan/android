package cn.izeno.android.presenter

import androidx.annotation.StringRes

/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/10/17
 */
interface ToastActivity {

  /**
   * @param text 提示内容
   * @param textRes 提示内容资源
   * @param button 按钮内容
   * @param buttonRes 按钮内容资源
   * @param action 点击按钮回调,  仅在传入 button/buttonRes 时生效
   */
  fun snack(
      text: String? = null, @StringRes textRes: Int? = null,
      button: String? = null, @StringRes buttonRes: Int? = null,
      action: (() -> Unit)? = null)


  fun toast(msg: String? = null, @StringRes msgRes: String? = null)
}