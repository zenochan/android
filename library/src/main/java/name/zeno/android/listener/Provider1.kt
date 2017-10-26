package name.zeno.android.listener

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/8/15
 */
@Deprecated("")
interface Provider1<R, T> {
  fun call(t: T): R
}
