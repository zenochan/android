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
    val content: ByteArray
    if (serializeConfig != null) {
      if (serializerFeatures != null) {
        content = JSON.toJSONBytes(value, serializeConfig, *serializerFeatures)
      } else {
        content = JSON.toJSONBytes(value, serializeConfig)
      }
    } else {
      if (serializerFeatures != null) {
        content = JSON.toJSONBytes(value, *serializerFeatures)
      } else {
        content = JSON.toJSONBytes(value)
      }
    }

    return RequestBody.create(MEDIA_TYPE, content)
  }

  companion object {
    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
  }
}
