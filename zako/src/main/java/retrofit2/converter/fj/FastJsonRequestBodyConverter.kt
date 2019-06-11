package retrofit2.converter.fj

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.serializer.SerializerFeature

import java.io.IOException

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

internal class FastJsonRequestBodyConverter<T>(
    private val serializeConfig: SerializeConfig?,
    features: Array<out SerializerFeature>?
) : Converter<T, RequestBody> {
  private val serializerFeatures: Array<out SerializerFeature>?

  init {
    serializerFeatures = features
  }

  @Throws(IOException::class)
  override fun convert(value: T): RequestBody {
    val content: ByteArray = when {
    // 字符串不做 json 转换, 否则 string 会被转换成 “string”
      value is String -> value.toByteArray()
      serializeConfig != null -> if (serializerFeatures != null) {
        JSON.toJSONBytes(value, serializeConfig, *serializerFeatures)
      } else {
        JSON.toJSONBytes(value, serializeConfig)
      }
      else -> if (serializerFeatures != null) {
        JSON.toJSONBytes(value, *serializerFeatures)
      } else {
        JSON.toJSONBytes(value)
      }
    }




    return RequestBody.create(MEDIA_TYPE, content)
  }

  companion object {
    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
  }
}
