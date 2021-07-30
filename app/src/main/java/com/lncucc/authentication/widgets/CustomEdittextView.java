package com.lncucc.authentication.widgets;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEdittextView extends AppCompatEditText {

    public CustomEdittextView(Context context) {
        super(context);
        init();
    }

    public CustomEdittextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEdittextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        InputFilter inputFilter = new InputFilter() {
//
//            Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");
//
//            @Override
//            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
//                Matcher matcher = pattern.matcher(charSequence);
//                if (!matcher.find()) {
//                    return null;
//                } else {
//                    Toasty.info(getContext(), "只能输入汉字,英文，数字");
//                    return "";
//                }
//
//            }
//        };
//        this.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(100)});
    }

}
