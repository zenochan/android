package retrofit2.converter.fj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;

import java.io.IOException;
import java.lang.reflect.Type;

import name.zeno.android.third.rxjava.ValueTransformer;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T>
{
  private static final String TAG = "FastJsonResponseBodyConverter";

  private static final Feature[] EMPTY_SERIALIZER_FEATURES = new Feature[0];

  private Type mType;

  private ParserConfig config;
  private int          featureValues;
  private Feature[]    features;

  private ValueTransformer<String, String> resTransformer;

  public void setResTransformer(ValueTransformer<String, String> resTransformer)
  {
    this.resTransformer = resTransformer;
  }

  public FastJsonResponseBodyConverter(Type type, ParserConfig config, int featureValues, Feature... features)
  {
    mType = type;
    this.config = config;
    this.featureValues = featureValues;
    this.features = features;
  }

  @Override
  public T convert(ResponseBody value) throws IOException
  {
    try {
      String res = value.string();
      if (resTransformer != null) {
        res = resTransformer.apply(res);
      }
      return JSON.parseObject(res, mType, config, featureValues, features != null ? features : EMPTY_SERIALIZER_FEATURES);
    } finally {
      value.close();
    }
  }
}
