package name.zeno.android.system

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.support.annotation.RequiresPermission
import android.support.v4.app.Fragment
import io.reactivex.Observable
import java.util.*

/**
 * Create Date: 16/7/5
 *
 * @author 陈治谋 (513500085@qq.com)
 */
class ContactHelper {

  private var activity: Activity? = null
  private var fragment: Fragment? = null

  private var onSelected: ((List<Contact>) -> Unit)? = null
  private var onCancelListener: (() -> Unit)? = null

  constructor(activity: Activity) {
    this.activity = activity
  }

  constructor(fragment: Fragment) {
    this.fragment = fragment
    this.activity = fragment.activity
  }

  @SuppressLint("MissingPermission")
  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode != REQUEST_SELECT_CONTACT) return

    if (resultCode == Activity.RESULT_OK && data != null) {
      val contactData = data.data
      val cursor = activity!!.managedQuery(contactData, null, null, null, null)
      // may cursor NPE
      if (cursor != null) {
        cursor.moveToFirst()
        val idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID)
        val contactId = cursor.getLong(idColumn)

        val contacts = contacts(activity!!, contactId)
        onSelected?.invoke(contacts)
      } else {
        onSelected?.invoke(emptyList())
      }
    } else {
      onCancelListener?.invoke()
    }
  }

  @RequiresPermission(Manifest.permission.READ_CONTACTS)
  fun select(next: (List<Contact>) -> Unit) {
    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
    if (null != fragment) {
      fragment!!.startActivityForResult(intent, REQUEST_SELECT_CONTACT)
    } else {
      activity!!.startActivityForResult(intent, REQUEST_SELECT_CONTACT)
    }

    onSelected = next
  }

  fun onCancel(onCancelListener: () -> Unit) {
    this.onCancelListener = onCancelListener
  }

  companion object {
    val REQUEST_SELECT_CONTACT = 0x100F

    // 查询的字段
    private val projection = arrayOf(ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, "sort_key", ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED, ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED, ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,

        ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER,
        //need api 16
        //ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
        ContactsContract.CommonDataKinds.Phone.DATA4, ContactsContract.CommonDataKinds.Phone.PHOTO_ID)

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    fun contactsSync(context: Context): Observable<List<Contact>> {
      return Observable.create { subscriber ->
        subscriber.onNext(contacts(context))
        subscriber.onComplete()
      }
    }

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    @JvmOverloads
    fun contacts(context: Context, concatId: Long? = null): List<Contact> {
      var cnd: String? = null
      if (concatId != null) {
        // contact_id = ？
        cnd = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + concatId
      }

      val r = ArrayList<Contact>()
      // 获取手机联系人
      val resolver = context.contentResolver
      val cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, cnd, null, null)
      cursor?.run {
        moveToFirst() // 游标移动到第一项
        for (i in 0 until count) {
          moveToPosition(i)
          r.add(Contact(
              id = getLong(0),
              displayName = getString(1),
              sortKey = getString(2),
              timesContacted = getInt(3),
              lastTimeContacted = getLong(4),
              lookup = getString(5),
              contactId = getLong(6),
              number = getString(7),
              normalizedNumber = getString(8)
          ))
        }
      }
      cursor?.close()
      return r
    }
  }
}
