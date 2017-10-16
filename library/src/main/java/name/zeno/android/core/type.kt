package name.zeno.android.core

import java.lang.reflect.*

/**
 * # 获取泛型 type
 * @author 陈治谋 (513500085@qq.com)
 * @since 2017/10/14
 * @see retrofit2.ServiceMethod.Builder.createCallAdapter
 */
fun ZType<*>.type(): Type {
  try {
    val type = this.javaClass.declaredMethods[2].parameterTypes[0];
    when {
      hasUnresolvableType(type) -> throw RuntimeException("Method return type must not include a type variable or wildcard: " + type);
      type == Void::class.java -> throw RuntimeException("Service methods cannot return void.");
    }
    return type;
  } catch (e: Throwable) {
    throw RuntimeException("Unable to create call adapter for $this", e)
  }
}

/**
 * # 检查是否有不确定的泛型
 * @see retrofit2.Utils.hasUnresolvableType
 */
private fun hasUnresolvableType(type: Type): Boolean {
  return when (type) {
    is Class<*> -> false
    is ParameterizedType -> type.actualTypeArguments.find { hasUnresolvableType(it) } != null
    is GenericArrayType -> hasUnresolvableType(type.genericComponentType)
    is TypeVariable<*> -> true
    is WildcardType -> true
    else -> {
      val className = type.javaClass.name
      throw IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <$type> is of type $className")
    }
  }
}
