package name.zeno.android.third.rxjava;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2016/11/29.
 */
public interface ValueTransformer<Upstream, Downstream>
{
  Downstream apply(Upstream upstream);
}
