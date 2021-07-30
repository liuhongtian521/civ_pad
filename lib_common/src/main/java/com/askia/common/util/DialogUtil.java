package com.askia.common.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


/**
 * This class is about dialog util.
 */
public class DialogUtil
{
    private DialogUtil()
    {
    }

    public static AlertDialog generateDialog(Context context, String content,
                                             DialogInterface.OnClickListener positiveListener)
    {
        return new AlertDialog.Builder(context)

                .setTitle("提示")
                .setMessage(content)
                .setPositiveButton("确定", positiveListener)
                .setNegativeButton("取消", null).create();
    }

    public static AlertDialog generateDialog(Context context, int content, int positive, int negative,
                                             DialogInterface.OnClickListener positiveListener,
                                             DialogInterface.OnClickListener negativeListener)
    {
        return new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(context.getString(content))
                .setPositiveButton(context.getString(positive), positiveListener)
                .setNegativeButton(context.getString(negative), negativeListener).create();
    }
}
