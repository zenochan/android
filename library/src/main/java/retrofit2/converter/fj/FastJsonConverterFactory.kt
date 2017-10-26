package retrofit2.converter.fj

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.alibaba.fastjson.parser.ParserConfig
import com.alibaba.fastjson.serializer.PropertyPreFilter
import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.serializer.SerializerFeature
import java.lang.reflect.Type

import name.zeno.android.third.rxjava.ValueTransformer
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit


/**
 * A [converter][Converter.Factory] which uses FastJson for JSON.
 *
 *
 * Because FastJson is so flexible in the types it supports, this converter assumes that it can
 * handle all types. If you are mixing JSON serialization with something else (such as protocol
 * buffers), you must [add][Retrofit.Builder.addConverterFactory] last to allow the other converters a chance to see their types.
 */
class FastJsonConverterFactory private constructor() : Converter.Factory() {

  private var mParserConfig = ParserConfig.getGlobalInstance()
  private var featureValues = JSON.DEFAULT_PARSER_FEATURE
  private var features: Array<Feature>? = null

  private var serializeConfig: SerializeConfig? = null
  private var serializerFeatures: Array<out SerializerFeature>? = null
  private val propertyPreFilter: PropertyPreFilter? = null


  private var resTransformer: ValueTransformer<String, String>? = null

  override fun responseBodyConverter(type: Type, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
    val converter = FastJsonResponseBodyConverter<Any>(type, mParserConfig, featureValues, features)
    converter.resTransformer = resTransformer
    return converter
  }

  override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?, methodAnnotations: Array<Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
    return FastJsonRequestBodyConverter<Any>(serializeConfig, serializerFeatures)
  }

  fun getParserConfig(): ParserConfig {
    return mParserConfig
  }

  fun setParserConfig(config: ParserConfig): FastJsonConverterFactory {
    this.mParserConfig = config
    return this
  }

  fun getParserFeatureValues(): Int {
    return featureValues
  }

  fun setParserFeatureValues(featureValues: Int): FastJsonConverterFactory {
    this.featureValues = featureValues
    return this
  }

  fun getParserFeatures(): Array<Feature>? {
    return features
  }

  fun setParserFeatures(features: Array<Feature>): FastJsonConverterFactory {
    this.features = features
    return this
  }

  fun getSerializeConfig(): SerializeConfig? {
    return serializeConfig
  }

  fun setSerializeConfig(serializeConfig: SerializeConfig): FastJsonConverterFactory {
    this.serializeConfig = serializeConfig
    return this
  }

  fun getSerializerFeatures(): Array<out SerializerFeature>? {
    return serializerFeatures
  }

  fun setSerializerFeatures(features: Array<SerializerFeature>): FastJsonConverterFactory {
    this.serializerFeatures = features
    return this
  }

  fun setResTransformer(resTransformer: ValueTransformer<String, String>) {
    this.resTransformer = resTransformer
  }

  companion object {

    /**
     * Create an default instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     *
     * @return The instance of FastJsonConverterFactory
     */
    fun create(): FastJsonConverterFactory {
      return FastJsonConverterFactory()
    }
  }
}

