package name.zeno.android.listener

import android.support.v4.widget.DrawerLayout
import android.view.View

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/5
 */
class DefaultDrawListener : DrawerLayout.DrawerListener {
  internal var onClosed: ((View) -> Unit)? = null
  internal var onSlide: ((View, slideOffset: Float) -> Unit)? = null
  internal var onOpened: ((View) -> Unit)? = null
  internal var onStateChanged: ((newState: Int) -> Unit)? = null

  override fun onDrawerClosed(drawerView: View) {
    onClosed?.invoke(drawerView)
  }

  override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    onSlide?.invoke(drawerView, slideOffset)
  }

  override fun onDrawerOpened(drawerView: View) {
    onOpened?.invoke(drawerView)
  }

  override fun onDrawerStateChanged(newState: Int) {
    onStateChanged?.invoke(newState)
  }

  fun onClosed(onClosed: (View) -> Unit): DefaultDrawListener {
    this.onClosed = onClosed
    return this
  }

  fun onSlide(onSlide: (View, slideOffset: Float) -> Unit): DefaultDrawListener {
    this.onSlide = onSlide
    return this
  }

  fun onOpened(onOpened: (View) -> Unit): DefaultDrawListener {
    this.onOpened = onOpened
    return this
  }

  fun onStateChanged(onStateChanged: (newState: Int) -> Unit): DefaultDrawListener {
    this.onStateChanged = onStateChanged
    return this
  }
}
