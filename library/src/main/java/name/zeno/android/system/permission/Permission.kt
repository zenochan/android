package name.zeno.android.system.permission

class Permission @JvmOverloads constructor(val name: String, val granted: Boolean, val shouldShowRequestPermissionRationale: Boolean = false) {

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o == null || javaClass != o.javaClass) return false

    val that = o as Permission?

    if (granted != that!!.granted) return false
    return if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) false else name == that.name
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + if (granted) 1 else 0
    result = 31 * result + if (shouldShowRequestPermissionRationale) 1 else 0
    return result
  }

  override fun toString(): String {
    return "Permission{" +
        "name='" + name + '\'' +
        ", granted=" + granted +
        ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
        '}'
  }
}
