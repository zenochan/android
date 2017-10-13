/*
 * The MIT License (MIT)
 * Copyright (c) 2016 baoyongzhang <baoyz94@gmail.com>
 */
package name.zeno.android.presenter.bigbang.action;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class CopyAction implements Action
{

  public static CopyAction create()
  {
    return new CopyAction();
  }

  @Override
  public void start(Context context, String text)
  {
    if (!TextUtils.isEmpty(text)) {
      ClipboardManager service = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      service.setPrimaryClip(ClipData.newPlainText("BigBang", text));
      copySuccess(context);
    }
  }

  public void copySuccess(Context context)
  {
    Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
  }
}
