package name.zeno.zako

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/test/router")
class ArouterActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_arouter)
    setResult(Activity.RESULT_CANCELED)
    setResult(Activity.RESULT_OK)
  }
}
