package com.askia.common.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.collection.LruCache;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Created by SongUp on 2018/1/21.
 */

public abstract class BaseVPAdapter<T> extends PagerAdapter
{
    protected List<T> dataList;
    //存放到Lrucache，用于清理不常用， 其中view.getTag key得到这是第几个View
    private LruCache<Integer, View> viewWeakHashMap;
    private LayoutInflater layoutInflater;
    private int converId;



    public BaseVPAdapter(Context context, int convertId, List<T> dataList) {
        this.dataList = dataList;
        this.converId = convertId;
        layoutInflater = LayoutInflater.from(context);
        viewWeakHashMap = new LruCache<>(20);
    }

    @Override
    public int getCount()
    {
        if(dataList == null)
            return 0;
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(container, position);
        container.addView(view);
        bindView(view, dataList.get(position));
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(getView(container, position));
    }

    //将布局添加到weakhashmap
    public View getView(ViewGroup container, int position){
        View view = viewWeakHashMap.get(position);
        if (view == null){
            view = layoutInflater.inflate(converId, container, false);
        }
        view.setTag(position);
        return view;
    }
    public abstract void bindView(View view, T data);

    public void notifyDataSetChanged(List<T> dataList)
    {
        this.dataList = dataList;
        super.notifyDataSetChanged();
    }
}
