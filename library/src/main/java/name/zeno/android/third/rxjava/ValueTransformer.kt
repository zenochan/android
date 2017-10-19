package name.zeno.android.third.rxjava

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/29.
 */
interface ValueTransformer<in Upstream, out Downstream> {
  fun apply(upstream: Upstream): Downstream
}
