package name.zeno.android.tint

import android.content.res.ColorStateList
import android.graphics.PorterDuff

class TintInfo(
    var tintList: ColorStateList? = null,
    var tintMode: PorterDuff.Mode? = null,
    var hasTintMode: Boolean = false,
    var hasTintList: Boolean = false
) {
}
