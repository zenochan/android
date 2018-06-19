package name.zeno.zako

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import name.zeno.android.presenter.ZActivity
import name.zeno.android.util.dp
import name.zeno.android.widget.Shadow
import name.zeno.android.widget.StatusBarView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.padding
import org.jetbrains.anko.verticalLayout

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/15
 */
@Route(path = "/app/shadow")
class ShadowActivity : ZActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    toast("1")
    verticalLayout {

      addView(StatusBarView(ctx))


      addView(Shadow(ctx).apply {
        cornersRadius = 16.dp
        color = Color.RED
        mode = Shadow.CORNERS
        borderColor = Color.WHITE
        borderWidth = 4.dp
      }.lparams(-1, 80.dp))

      addView(View(ctx).apply {
        backgroundColor = Color.WHITE
      }.lparams {
        width = 16.dp
        height = 5.dp
        topMargin = 1.dp
      })
    }
  }

}
