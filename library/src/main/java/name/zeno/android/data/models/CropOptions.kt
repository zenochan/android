package name.zeno.android.data.models

/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/7/5
 * @see [关于Android实现裁剪功能总结](https://blog.csdn.net/superxlcr/article/details/70209774)
 */

class CropOptions(
    var crop: Boolean = true,
    var aspectX: Int = 1,
    var aspectY: Int = 1,
    var outputX: Int = 300,
    var outputY: Int = 300,
    var returnData: Boolean = false,
    var scale: Boolean = true,
    var circleCrop: Boolean = false
)
