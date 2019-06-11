package cn.izeno.android.widget.sort

/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/7/24
 */
class SortField(
    var name: String? = null,
    var filed: String? = null,
    @field:SortInt
    var sort: Int = 0,
    var weight: Float = 1f
) {
  var ascEnable = true
  var descEnable = true

  fun disableAsc() = apply {
    ascEnable = false
  }

  fun disableDesc() = apply {
    descEnable = false
  }

  // 自定义的drawable right
  fun drawableRes(@SortInt sort: Int): Int? {
    return null
  }

  companion object {
    const val NONE = 0
    const val ASC = 1
    const val DESC = 2
  }

}