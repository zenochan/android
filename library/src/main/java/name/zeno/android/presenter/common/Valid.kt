package name.zeno.android.presenter.common

import name.zeno.android.common.annotations.DataClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/7.
 */
@DataClass data class Valid(
    var isValid: Boolean = false,
    var info: String? = null
)
