package name.zeno.zako

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import name.zeno.android.jiguang.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    jPushAddTags("SX")
        .compose { jPushCheckTagBindState("SX") }
        .subscribe(
            { Log.e("Zeno", "bind to tag [SX] --> $it") },
            { Log.e("Error", "Error", it) }
        )

    jPushSetMobile("18751943557").subscribe {
      Log.e("Zeno", "mobile -> $it")
    }
  }
}
