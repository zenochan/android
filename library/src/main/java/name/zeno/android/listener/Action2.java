package name.zeno.android.listener;

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 16/6/23
 */
public interface Action2<T1, T2>
{
  void call(T1 t1, T2 t2);
}
