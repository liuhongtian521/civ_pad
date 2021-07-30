package com.lncucc.authentication.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.askia.common.base.ViewManager;
import com.lncucc.authentication.R;


/**
 * Created by qinyy on 2018/2/17.
 */
public class CommonTitleBar extends RelativeLayout
{
    TextView mTvTitle;
    RelativeLayout mRlLeftBack;
    TextView mTvRightBtn;
    RelativeLayout mRelativeLayout;
    private boolean alreadyExit = false;

    private Context mContext;
    private String mTitleContent;



    public CommonTitleBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.common_title_layout, this);

        mTvTitle = findViewById(R.id.tv_title);
        mRlLeftBack = findViewById(R.id.rl_left_back);
        mRelativeLayout = findViewById(R.id.rl_container);
        //attrs
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
        mTitleContent = typedArray.getString(R.styleable.CommonTitleBar_title);
        mTvTitle.setText(mTitleContent);

        int color = typedArray.getColor(R.styleable.CommonTitleBar_color,mContext.getResources().getColor(R.color.theme));
        mRelativeLayout.setBackgroundColor(color);
        if(typedArray.getBoolean(R.styleable.CommonTitleBar_goback,true))
            mRlLeftBack.setVisibility(VISIBLE);
        else
            mRlLeftBack.setVisibility(GONE);
        mRlLeftBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alreadyExit)
                    return;
                alreadyExit = true;
                ViewManager.getInstance().currentActivity().finish();
            }
        });


        int height = 0;
        int resourceId = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }
        mRelativeLayout.setPadding(0,height,0,0);

    }

    public void setColor(@ColorInt int color)
    {
        mRelativeLayout.setBackgroundColor(color);
    }

    public CommonTitleBar(Context context)
    {
        super(context);
        mContext = context;
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.common_title_layout, this);
        //attrs
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CommonTitleBar);
        mTitleContent = typedArray.getString(R.styleable.CommonTitleBar_title);
        mTvTitle.setText(mTitleContent);
    }


    public void setBackVisible()
    {
        mRlLeftBack.setVisibility(VISIBLE);
    }

    public void setBackGone()
    {
        mRlLeftBack.setVisibility(GONE);
    }

    public void initRightTextView(String hint,OnClickListener clickListener)
    {
        mTvRightBtn.setVisibility(VISIBLE);
        mTvRightBtn.setText(hint);
        mTvRightBtn.setClickable(true);
        mTvRightBtn.setOnClickListener(clickListener);
    }

    public void setTvTitle(String title)
    {
        mTvTitle.setText(title);
    }

    /*public void setSearchVisible(OnSearchCallback onSearchCallback)
    {
        mSearchLayout.setVisibility(VISIBLE);
        mTvTitle.setVisibility(GONE);
        mEdtTitleSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && onSearchCallback != null)
                {
                    onSearchCallback.onOnSearchCallback(v.getEditableText().toString());
                    return true;
                }
                return false;
            }
        });
    }*/

/*    public interface OnSearchCallback
    {
        void onOnSearchCallback(String content);
    }*/

}
