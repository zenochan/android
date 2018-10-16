package name.zeno.zako

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import name.zeno.android.presenter.ZActivity
import name.zeno.android.presenter.cropByNative
import name.zeno.android.third.rxjava.ZObserver
import name.zeno.android.util.PhotoCaptureHelper
import name.zeno.android.util.dp
import name.zeno.android.widget.StatusBarView
import name.zeno.ktrxpermission.ZPermission
import name.zeno.ktrxpermission.rxPermissions
import org.jetbrains.anko.button
import org.jetbrains.anko.imageView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.verticalLayout


/**
 * @author Zeno [mail](mailto:zenochan@qq.com)
 * @since 2018/7/5
 */
@Route(path = "/app/avatar")
class AvatarActivity : ZActivity() {
  val capture by lazy { PhotoCaptureHelper(this) }
  lateinit var iv: ImageView;

  @SuppressLint("MissingPermission")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    capture.listener = {
      Glide.with(this@AvatarActivity).load(it).into(iv)
      clip(it)
    }

    verticalLayout {
      addView(StatusBarView(this@AvatarActivity))
      iv = imageView {
      }.lparams(200.dp, 200.dp)


      button {
        text = "相册"
        onClick {
          rxPermissions(ZPermission.READ_EXTERNAL_STORAGE).subscribe { granted ->
            if (granted) capture.getImageFromAlbum()
          }
        }
      }

      button {
        text = "相机"
        onClick {
          rxPermissions(
              ZPermission.READ_EXTERNAL_STORAGE,
              ZPermission.CAMERA
          ).subscribe { granted ->
            if (granted) capture.getImageFromCamera()
          }
        }
      }


      button {
        text = "ID Card"
        onClick {
          rxPermissions(
              ZPermission.WRITE_EXTERNAL_STORAGE,
              ZPermission.CAMERA
          ).subscribe { granted ->
            if (granted) capture.getIdCardFromCamera()
          }
        }
      }
    }
  }

  fun clip(path: String) {
    cropByNative(path).subscribe(ZObserver.next { Glide.with(this).load(it).into(iv) })

//    nav<BaseData>(ClipActivity::class, BaseData(string = path)) { ok, data ->
//      if (ok && data != null) {
//        Glide.with(this).load(data.string).into(iv)
//      }
//    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    capture.onActivityResult(requestCode, resultCode, data)
  }

  override fun onDestroy() {
    super.onDestroy()
    capture.onDestroy()
  }

}