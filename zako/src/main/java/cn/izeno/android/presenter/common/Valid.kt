package cn.izeno.android.presenter.common

import cn.izeno.android.common.annotations.DataClass

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/7.
 */
@DataClass data class Valid(
    var isValid: Boolean = false,
    var info: String? = null
)
