package name.zeno.android.jiguang

import android.app.Application
import android.content.Context
import android.util.Log
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.JPushMessage
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * 初始化 JPush， 在子线程中完成
 */
fun Application.jPushInit(debug: Boolean? = null) {
  Single.create<Long> {
    val start = System.currentTimeMillis();
    // ~ 240ms
    if (debug != null) JPushInterface.setDebugMode(debug)
    JPushInterface.init(this)
    it.onSuccess(System.currentTimeMillis() - start)
  }.subscribeOn(Schedulers.io()).subscribe { t ->
    Log.e("JPUSH", "initialized in $t ms")
  }
}

/**
 *
 * 停止推送服务。
 * 用来检查 Push Service 是否已经被停止
 *
 * 调用了本 API 后，JPush 推送服务完全被停止。具体表现为：
 * - 收不到推送消息
 * - 极光推送所有的其他 API 调用都无效,不能通过 JPushInterface.init 恢复，需要调用resumePush恢复。
 *
 * 恢复推送服务。
 * 调用了此 API 后，极光推送完全恢复正常工作。
 */
var Context.jPushStop
  get() = JPushInterface.isPushStopped(this.applicationContext)
  set(value) {
    if (value) JPushInterface.stopPush(this.applicationContext)
    else JPushInterface.resumePush(this.applicationContext)
  }

val Context.jPushId
  get() = JPushInterface.getRegistrationID(this)

/**
 *  @param alias 每次调用设置有效的别名，覆盖之前的设置。
 *  - 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
 *  - 限制：alias 命名长度限制为 40 字节。（判断长度需采用UTF-8编码）
 */
fun Context.jPushSetAlias(alias: String): Observable<String> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.ALIAS_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.alias
      }
      .doOnSubscribe { JPushInterface.setAlias(this, sequence, alias) }
}

/**
 *  @param alias 获取别名
 */
fun Context.jPushGetAlias(): Observable<String> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.ALIAS_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.alias
      }
      .doOnSubscribe { JPushInterface.getAlias(this, sequence) }
}


/**
 *  删除别名,  如用户退出登录的时候
 */
fun Context.jPushDeleteAlias(): Observable<String> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.ALIAS_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.alias
      }
      .doOnSubscribe { JPushInterface.deleteAlias(this, sequence) }
}

/**
 * 设置标签。覆盖旧设置
 * @param tags 每次调用至少设置一个 tag，覆盖之前的设置，不是新增。
 * - 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
 * - 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
 * - 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
 */
fun Context.jPushSetTags(vararg tags: String): Observable<Set<String>> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.TAG_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.tags
      }
      .doOnSubscribe { JPushInterface.setTags(this, sequence, tags.toSet()) }
}

/**
 * 新增标签。
 * @param tags 每次调用至少设置一个 tag
 * - 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
 * - 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
 * - 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
 */
fun Context.jPushAddTags(vararg tags: String): Observable<Set<String>> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.TAG_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.tags
      }
      .doOnSubscribe { JPushInterface.addTags(this, sequence, tags.toSet()) }
}


/**
 * 删除 tags
 * @param tags 每次调用至少设置一个 tag
 * - 有效的标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
 * - 限制：每个 tag 命名长度限制为 40 字节，最多支持设置 1000 个 tag，且单次操作总长度不得超过5000字节。（判断长度需采用UTF-8编码）
 * - 单个设备最多支持设置 1000 个 tag。App 全局 tag 数量无限制。
 */
fun Context.jPushDeleteTags(vararg tags: String): Observable<Set<String>> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.TAG_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.tags
      }
      .doOnSubscribe { JPushInterface.deleteTags(this, sequence, tags.toSet()) }
}

/**
 * 清除 tags
 */
fun Context.jPushCleanTags(): Observable<Set<String>> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.TAG_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.tags
      }
      .doOnSubscribe { JPushInterface.cleanTags(this, sequence) }
}

/**
 * 获取所有 tags
 */
fun Context.jPushGetAllTags(): Observable<Set<String>> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.TAG_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.tags
      }
      .doOnSubscribe { JPushInterface.getAllTags(this, sequence) }
}

/**
 * 查询指定 tag 与当前用户绑定的状态
 */
fun Context.jPushCheckTagBindState(tag: String): Observable<Boolean> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.CHECK_TAG_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.tagCheckStateResult
      }
      .doOnSubscribe { JPushInterface.checkTagBindState(this, sequence, tag) }
}

/**
 * 设置手机号码接口,用于[短信补充功能](https://docs.jiguang.cn/jpush/guideline/push-SMS-intro/)。
 * > 短信补充仅支持国内业务，号码格式为 11位数字，有无 +86 前缀皆可。
 *
 * @param mobile 手机号码。如果传null或空串则为解除号码绑定操作。
 * 限制：只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。
 */
fun Context.jPushSetMobile(mobile: String?): Observable<String> {
  val sequence = JpushSequence.new()
  return RxJPushMessageReceiver.MOBILE_NUMBER_OPERATOR_RESULT
      .filter { it.sequence == sequence }
      .map {
        if (it.errorCode != 0) throw JPushException(it.errorCode)
        it.mobileNumber ?: ""
      }
      .doOnSubscribe { JPushInterface.setMobileNumber(this, sequence, mobile) }
}

private object JpushSequence {
  private var counter = 0
  @Synchronized
  fun new() = counter++
}

