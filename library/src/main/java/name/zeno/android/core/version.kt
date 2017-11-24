package name.zeno.android.core

import android.os.Build

val sdkInt = Build.VERSION.SDK_INT
const val ICE_CREAM_SANDWICH_MR1 = 15;
const val JELLY_BEAN = 16;
/** __API 17(4.2)__ November 2012: Android 4.2, Moar jelly beans! */
const val JELLY_BEAN_MR1 = 17;
/** __API 18(4.3)__ July 2013: Android 4.3, the revenge of the beans. */
const val JELLY_BEAN_MR2 = 18;
/** __API 19(4.4)__ */
const val KITKAT = 19;
/** June 2014: Android 4.4W. KitKat for watches, snacks on the run. */
const val KITKAT_WATCH = 20;
/** 5.0 */
const val LOLLIPOP = 21;
/** March 2015: Lollipop with an extra sugar coating on the outside! */
const val LOLLIPOP_MR1 = 22;
/** 6.0 */
const val M = 23;
/** 7.0 */
const val N = 24;
const val N_MR1 = 25;
/** 8.0 */
const val O = 26;
/** 8.1 */
const val O_MR1 = 27;

/** 5.0+ */
val lollipop = sdkInt >= LOLLIPOP
/**
 * # 6.0+
 *
 * [Android 6.0 新特性详解](http://www.jianshu.com/p/30fb0c1c88ad)
 * - 运行时权限 Runtime Permissions
 *    - 对于一个敏感权限来说，他有两条命
 *    - 第一条命是在第一次显示该权限的时候，如果这个使用用户点击拒绝，那么第一条命就没了
 *    - 当App第二次使用到这个权限的时候，界面会多出一个『不再询问』的选项，如果用户这里再继续拒绝，或者勾选『不再询问』，那么第二条命就Game Over了
 *    - 只能让用户去Setting中手动去管理这个权限了。
 *
 */
val marshmallow: Boolean = sdkInt >= M
