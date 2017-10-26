package name.zeno.android.listener

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/23
 */
@Deprecated("")
interface Action2<T1, T2> {
  fun call(t1: T1, t2: T2)
}
