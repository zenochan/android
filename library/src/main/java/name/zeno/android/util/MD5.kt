package name.zeno.android.util

object MD5 {

  @Deprecated("use Encode#md5(String)")
  fun md5(string: String): String? {
    return Encode.md5(string)
  }


  @Deprecated("use Encode#md5(byte[])")
  fun md5(buffer: ByteArray): String? {
    return Encode.md5(buffer)
  }
}
