package com.lncucc.authentication.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by qinyy on 2019/4/14.
 */

public class AutoSizeWebview extends WebView
{

    public AutoSizeWebview(Context context)
    {
        super(context);
    }

    public AutoSizeWebview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AutoSizeWebview(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOverScrollMode(int mode) {
        super.setOverScrollMode(mode);
       // AutoSize.autoConvertDensityOfGlobal();
    }
}
