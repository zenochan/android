package name.zeno.android.system;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import lombok.Setter;
import name.zeno.android.listener.Action0;
import name.zeno.android.listener.Action1;

/**
 * Create Date: 16/7/5
 *
 * @author 陈治谋 (513500085@qq.com)
 */
public class ContactHelper
{
  public static final int REQUEST_SELECT_CONTACT = 0x100F;

  private Activity activity;
  private Fragment fragment;

  private         Action1<List<Contact>> onSelected;
  @Setter private Action0                onCancelListener;

  public ContactHelper(Activity activity)
  {
    this.activity = activity;
  }

  public ContactHelper(Fragment fragment)
  {
    this.fragment = fragment;
    this.activity = fragment.getActivity();
  }

  // 查询的字段
  private static String[] projection = {
      ContactsContract.CommonDataKinds.Phone._ID,
      ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
      "sort_key",
      ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED,
      ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED,
      ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,

      ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
      ContactsContract.CommonDataKinds.Phone.NUMBER,
      //need api 16
      //ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
      ContactsContract.CommonDataKinds.Phone.DATA4,
      ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
  };

  @RequiresPermission(Manifest.permission.READ_CONTACTS)
  public static Observable<List<Contact>> contactsSync(Context context)
  {
    return Observable.create(subscriber -> {
      subscriber.onNext(contacts(context));
      subscriber.onComplete();
    });
  }

  @RequiresPermission(Manifest.permission.READ_CONTACTS)
  public static List<Contact> contacts(Context context)
  {
    return contacts(context, null);
  }

  @RequiresPermission(Manifest.permission.READ_CONTACTS)
  public static List<Contact> contacts(Context context, Long concatId)
  {
    String cnd = null;
    if (concatId != null) {
      cnd = String.format("%s=%d", ContactsContract.CommonDataKinds.Phone.CONTACT_ID, concatId);
    }

    List<Contact> r = new ArrayList<>();
    // 获取手机联系人
    ContentResolver resolver = context.getContentResolver();
    Cursor          cursor   = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, cnd, null, null);
    if (cursor != null) {
      cursor.moveToFirst(); // 游标移动到第一项
      for (int i = 0; i < cursor.getCount(); i++) {
        cursor.moveToPosition(i);

        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        contact.setDisplayName(cursor.getString(1));
        contact.setSortKey(cursor.getString(2));
        contact.setTimesContacted(cursor.getInt(3));
        contact.setLastTimeContacted(cursor.getLong(4));
        contact.setLookup(cursor.getString(5));
        contact.setContactId(cursor.getLong(6));
        contact.setNumber(cursor.getString(7));
        contact.setNormalizedNumber(cursor.getString(8));

        r.add(contact);
      }
      cursor.close();
    }

    return r;
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (requestCode == REQUEST_SELECT_CONTACT) {
      if (resultCode == Activity.RESULT_OK) {

        Uri    contactData = data.getData();
        Cursor cursor      = activity.managedQuery(contactData, null, null, null, null);
        cursor.moveToFirst();
        int  idColumn  = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        long contactId = cursor.getLong(idColumn);
        //noinspection MissingPermission
        List<Contact> contacts = contacts(activity, contactId);
        if (onSelected != null) {
          onSelected.call(contacts);
        }
      } else if (onCancelListener != null) {
        onCancelListener.call();
      }
    }
  }

  @RequiresPermission(Manifest.permission.READ_CONTACTS)
  public void select(Action1<List<Contact>> next)
  {
    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    if (null != fragment) {
      fragment.startActivityForResult(intent, REQUEST_SELECT_CONTACT);
    } else {
      activity.startActivityForResult(intent, REQUEST_SELECT_CONTACT);
    }

    onSelected = next;
  }
}
