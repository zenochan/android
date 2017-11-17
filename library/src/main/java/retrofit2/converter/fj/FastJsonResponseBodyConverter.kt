package retrofit2.converter.fj

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig

import java.io.IOException
import java.lang.reflect.Type

import name.zeno.android.third.rxjava.ValueTransformer
import okhttp3.ResponseBody
import retrofit2.Converter

class FastJsonResponseBodyConverter<T>(
    private val mType: Type,
    private val config: ParserConfig,
    private val featureValues: Int,
    private val features: Array<Feature>? = null
) : Converter<ResponseBody, T> {

  var resTransformer: ((String) -> String)? = null

  @Throws(IOException::class)
  override fun convert(value: ResponseBody): T {
    try {
      var res = value.string()
      if (resTransformer != null) {
        res = resTransformer?.invoke(res)
      }
      return JSON.parseObject(res, mType, config, featureValues, *features ?: EMPTY_SERIALIZER_FEATURES)
    } finally {
      value.close()
    }
  }

//  fun setResTransformer(resTransformer: ValueTransformer<String, String>) {
//    this.resTransformer = resTransformer
//  }

  companion object {
    private val EMPTY_SERIALIZER_FEATURES = arrayOfNulls<Feature>(0)
  }
}
