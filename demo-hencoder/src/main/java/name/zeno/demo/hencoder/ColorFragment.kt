package name.zeno.demo.hencoder

import android.app.Fragment
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_color.*
import name.zeno.android.uicore.ShaderFactory
import name.zeno.android.uicore.colorFilter
import name.zeno.android.uicore.shader
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/11/10
 */
class ColorFragment : Fragment(), Title {

  override val title: String = "颜色"
  private var mode = Shader.TileMode.CLAMP
  private var checkedId = 0
  private var colorMaskMul = Color.WHITE
  private var colorMaskAdd = Color.BLACK

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_color, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    tileModeRG.onCheckedChange { group, checkedId ->
      mode = when (checkedId) {
        clamp.id -> Shader.TileMode.CLAMP
        mirror.id -> Shader.TileMode.MIRROR
        else -> Shader.TileMode.REPEAT
      }
      invalidate()
    }

    shaderRG.onCheckedChange { _, checkedId ->
      this@ColorFragment.checkedId = checkedId
      invalidate()
    }

    val listener = object : SeekBar.OnSeekBarChangeListener {
      override fun onProgressChanged(p0: SeekBar, p1: Int, p2: Boolean) {
        if (!p2) return
        val color = when (p0) {
          mulR, addR -> Color.rgb(p1, 0, 0)
          mulG, addG -> Color.rgb(0, p1, 0)
          mulB, addB -> Color.rgb(0, 0, p1)
          else -> 0x000000
        }
        p0.progressDrawable = ColorDrawable(color)

        when (p0) {
          mulR, mulG, mulB -> colorMaskMul = Color.rgb(mulR.progress, mulG.progress, mulB.progress)
          else -> colorMaskAdd = Color.rgb(addR.progress, addG.progress, addB.progress)
        }
        invalidate()
      }


      override fun onStartTrackingTouch(p0: SeekBar) {}
      override fun onStopTrackingTouch(p0: SeekBar) {
        p0.invalidate()
      }
    }
    mulR.setOnSeekBarChangeListener(listener)
    mulG.setOnSeekBarChangeListener(listener)
    mulB.setOnSeekBarChangeListener(listener)

    addR.setOnSeekBarChangeListener(listener)
    addG.setOnSeekBarChangeListener(listener)
    addB.setOnSeekBarChangeListener(listener)

  }

  private fun invalidate() = with(shaderView) {
    val colorStart = Color.BLUE
    val colorEnd = Color.CYAN
    val cx = width / 2
    val cy = height / 2
    val dash = dip(24)

    paint.shader {
      when (checkedId) {
        linear.id -> linearGradient(cx - dash, cy / 2, cx + dash, cy + cy / 2, colorStart, colorEnd, mode)
        radial.id -> radialGradient(cx, cy, dash, colorStart, colorEnd, mode)
        sweep.id -> sweepGradient(cx, cy, colorStart, colorEnd)
        bitmap.id -> {
          bitmapShader(BitmapFactory.decodeResource(resources, R.drawable.aguai), mode, mode)
        }
        compose.id -> ShaderFactory.compose(
            linearGradient(cx - dash, cy / 2, cx + dash, cy + dash, colorStart, colorEnd, mode),
            bitmapShader(BitmapFactory.decodeResource(resources, R.drawable.ic_pre), mode, mode),
            PorterDuff.Mode.XOR
        )
        else -> null
      }
    }

    paint.colorFilter { light(colorMaskMul, colorMaskAdd) }
    invalidate()
  }
}