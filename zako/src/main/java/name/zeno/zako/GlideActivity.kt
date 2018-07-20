package name.zeno.zako

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import name.zeno.android.presenter.ZActivity
import name.zeno.android.third.glide.GlideApp
import name.zeno.android.util.dp
import org.jetbrains.anko.imageView
import org.jetbrains.anko.verticalLayout

/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/7/9
 */
@Route(path = "/app/glide")
class GlideActivity : ZActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    verticalLayout {
      imageView {
        GlideApp.with(this@GlideActivity)
            .load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2535451895,284003823&fm=27&gp=0.jpg")
            .into(this)
      }.lparams(200.dp,200.dp)

    }
  }
}