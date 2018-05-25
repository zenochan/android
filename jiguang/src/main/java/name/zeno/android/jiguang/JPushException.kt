package name.zeno.android.jiguang

/**
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/5/25
 */

class JPushException(val code: Int) : Exception("[$code] ${JPushException.INFO[code]}") {
  companion object {
    val INFO = HashMap<Int, String>().apply {
      this[6002] = "设置超时"
      this[6003] = "alias 字符串不合法"
      this[6004] = "alias超长。最多 40个字节"
      this[6005] = "某一个 tag 字符串不合法"
      this[6006] = "某一个 tag 超长。一个 tag 最多 40个字节"
      this[6007] = "tags 数量超出限制。最多 1000个"
      this[6008] = "tag 超出总长度限制"
      this[6009] = "未知错误"

      this[6011] = "10s内设置tag或alias大于10次，或10s内设置手机号码大于3次"
      this[6012] = "在JPush服务stop状态下设置了tag或alias或手机号码"
      this[6013] = "用户设备时间轴异常"
      this[6014] = "服务器繁忙,建议重试"
      this[6015] = "appkey 在黑名单中"
      this[6016] = "无效用户"
      this[6017] = "无效请求"
      this[6018] = "后台累计设置的tag数超过1000个,建议先清除部分tag"
      this[6019] = "查询请求已过期"
      this[6020] = "tag/alias操作暂停,建议过一段时间再设置"
      this[6021] = "tags操作正在进行中，暂时不能进行其他tags操作"
      this[6022] = "alias操作正在进行中，暂时不能进行其他alias操作"
      this[6023] = "手机号码不合法"
      this[6024] = "服务器内部错误"
      this[6025] = "手机号码太长"
    }
  }
}