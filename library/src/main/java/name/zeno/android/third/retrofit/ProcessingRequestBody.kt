package name.zeno.android.third.retrofit

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * 监听上传进度
 * - [observable] 进度监听
 *
 * @author 陈治谋 (513500085@qq.com)
 * @since 2018/1/24
 */
open class ProcessingRequestBody(private var delegate: RequestBody) : RequestBody() {
  private var processSub = PublishSubject.create<Progress>()

  val observable: Observable<Progress> = processSub
      .subscribeOn(AndroidSchedulers.mainThread())


  override fun contentType(): MediaType? = delegate.contentType()

  @Throws(IOException::class)
  override fun contentLength(): Long = delegate.contentLength()

  @Throws(IOException::class)
  override fun writeTo(sink: BufferedSink) {
    val countingSink = CountingSink(sink)
    val bufferedSink = Okio.buffer(countingSink)
    delegate.writeTo(bufferedSink)
    bufferedSink.flush()
  }

  /**
   * 1. 重写 write 方法，额外记录已写入的 bytes 数
   * 2. 通知进度变化、完成
   */
  private inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
    private var bytesWritten: Long = 0

    @Throws(IOException::class)
    override fun write(source: Buffer, byteCount: Long) {
      super.write(source, byteCount)
      bytesWritten += byteCount
      processSub.onNext(Progress(contentLength(), bytesWritten))
      if (bytesWritten == contentLength()) processSub.onComplete()
    }
  }
}
