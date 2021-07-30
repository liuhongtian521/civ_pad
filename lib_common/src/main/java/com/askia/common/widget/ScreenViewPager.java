package com.askia.common.widget;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by lch on 2017/5/8.
 */
public class ScreenViewPager extends ViewPager {
    public ScreenViewPager(Context context) {
        super(context);
        initialize();
    }

    public ScreenViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        setChildrenDrawingOrderEnabled(true);
        setOffscreenPageLimit(5);
        setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                float pageWidth = page.getWidth();
                float v = Math.abs(position);
                float v1 = 0.2f * (v * v);
                page.setAlpha(1f);
                if (position > 2) {
                    page.setAlpha(0);
                } else if (position > 1) {
                    page.setScaleX(0.8f);
                    page.setScaleY(0.8f);
                    page.setAlpha(Math.abs(2 - position));
                    page.setTranslationX(-v / 2 * pageWidth);
                } else if (position >= 0) {
                    page.setScaleX(1 - v1);
                    page.setScaleY(1 - v1);
                    page.setTranslationX(-v / 2 * pageWidth);
                } else if (position >= -1) {
                    page.setScaleX(1 - v1);
                    page.setScaleY(1 - v1);
                    page.setTranslationX(v / 2 * pageWidth);
                } else {
                    page.setScaleX(0.8f);
                    page.setScaleY(0.8f);
                    page.setAlpha(2 - Math.abs(position));
                    page.setTranslationX(v / 2 * pageWidth);
                }
            }
        });
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int position = getCurrentItem();
        if(position<0){
            return i;
        }else{
            if(i == childCount - 1){//这是最后一个需要刷新的item
                return position;
            }
            if(i == position){
                return childCount - 1;
            }
        }
        return i;//正常次序的item

    }
}
