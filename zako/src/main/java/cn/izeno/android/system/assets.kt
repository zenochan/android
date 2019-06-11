package cn.izeno.android.system

import android.app.Activity
import android.app.Fragment
import cn.izeno.android.util.IOUtils
import org.jetbrains.anko.ctx

/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/7/19
 */

/**
 * 获取 assets 下的文本文件内容
 */
fun Activity.assets(file: String) = IOUtils.readString(this, file)

/**
 * 获取 assets 下的文本文件内容
 */
fun Fragment.assets(file: String) = IOUtils.readString(this.ctx, file)

