package name.zeno.android.system.permission

import name.zeno.android.common.annotations.DataClass

@DataClass data class Permission @JvmOverloads constructor(
    val name: String,
    val granted: Boolean,
    val shouldShowRequestPermissionRationale: Boolean = false
)
