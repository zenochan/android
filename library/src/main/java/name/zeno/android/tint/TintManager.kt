package name.zeno.android.tint

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.Log
import androidx.collection.LruCache

object TintManager {

  private val TAG = "EmTintManager"

  private val COLOR_FILTER_CACHE = ColorFilterLruCache(6)

  private val DEFAULT_MODE = PorterDuff.Mode.SRC_IN

  fun tintDrawable(drawable: Drawable, tint: TintInfo, state: IntArray) {
    if (shouldMutateBackground(drawable) && drawable.mutate() !== drawable) {
      Log.d(TAG, "Mutated drawable is not the same instance as the input.")
      return
    }

    if (tint.hasTintList || tint.hasTintMode) {
      drawable.colorFilter = createTintFilter(
          if (tint.hasTintList) tint.tintList else null,
          if (tint.hasTintMode) tint.tintMode else DEFAULT_MODE,
          state)
    } else {
      drawable.clearColorFilter()
    }
  }

  private fun shouldMutateBackground(drawable: Drawable): Boolean = when {
    Build.VERSION.SDK_INT >= 16 -> true
    drawable is LayerDrawable -> Build.VERSION.SDK_INT >= 16
    drawable is InsetDrawable -> true
    drawable is DrawableContainer -> {
      // If we have a DrawableContainer, let's traverse it's child array
      val state = drawable.getConstantState()
      if (state is DrawableContainer.DrawableContainerState) {
        state.children.all { shouldMutateBackground(it) }
      } else true
    }
    else -> true
  }


  private fun createTintFilter(tint: ColorStateList?, tintMode: PorterDuff.Mode?, state: IntArray): PorterDuffColorFilter? {
    if (tint == null || tintMode == null) {
      return null
    }
    val color = tint.getColorForState(state, Color.RED)
    return getPorterDuffColorFilter(color, tintMode)
  }

  private fun getPorterDuffColorFilter(color: Int, mode: PorterDuff.Mode): PorterDuffColorFilter {
    // First, lets see if the cache already contains the color filter
    var filter: PorterDuffColorFilter? = COLOR_FILTER_CACHE.get(color, mode)

    if (filter == null) {
      // Cache miss, so create a color filter and add it to the cache
      filter = PorterDuffColorFilter(color, mode)
      COLOR_FILTER_CACHE.put(color, mode, filter)
    }

    return filter
  }

  /** ColorFilterLruCache */
  private class ColorFilterLruCache(maxSize: Int) : LruCache<Int, PorterDuffColorFilter?>(maxSize) {
    internal fun get(color: Int, mode: PorterDuff.Mode) = get(key(color, mode))
    internal fun put(color: Int, mode: PorterDuff.Mode, filter: PorterDuffColorFilter) = put(key(color, mode), filter)

    private fun key(color: Int, mode: PorterDuff.Mode): Int {
      var hashCode = 1
      hashCode = 31 * hashCode + color
      hashCode = 31 * hashCode + mode.hashCode()
      return hashCode
    }
  }
}
