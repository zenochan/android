package name.zeno.android.uicore

import android.graphics.ComposeShader
import android.graphics.PorterDuff
import android.graphics.Shader
import android.graphics.Xfermode

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/9
 */


/** shader 类型相同时需要关闭硬件加速, 否则木有效果 */
fun Shader.compose(shader: Shader, mode: Xfermode) = ComposeShader(this, shader, mode)

/** shader 类型相同时需要关闭硬件加速, 否则木有效果 */
fun Shader.compose(shader: Shader, mode: PorterDuff.Mode) = ComposeShader(this, shader, mode)
