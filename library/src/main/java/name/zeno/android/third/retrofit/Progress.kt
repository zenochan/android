package name.zeno.android.third.retrofit

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/1/25
 */
data class Progress(
    var total: Long = 0,
    var process: Long = -1
) {
  val progress: Float
    get() {
      if (this.process == 0L) this.process = -1
      return process.toFloat() / total
    }

  fun set(total: Long, process: Long) {
    this.total = total
    this.process = process
  }
}