package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataServicePageAdapter;
import com.lncucc.authentication.databinding.ActSystemSettingBinding;
import com.lncucc.authentication.fragments.BaseSettingFragment;
import com.lncucc.authentication.fragments.AdvancedSettingFragment;
import com.lncucc.authentication.fragments.NetworkSettingFragment;
import com.lncucc.authentication.fragments.DisplaySettingFragment;
import com.lncucc.authentication.fragments.LogoutSettingFragment;
import com.lncucc.authentication.fragments.DateSettingFragment;
import com.lncucc.authentication.widgets.NoSwipeViewPager;

import java.util.ArrayList;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * 数据服务
 */
@Route(path = ARouterPath.SYSTEM_SETTING)
public class SystemSettingActivity extends BaseActivity {

    private ActSystemSettingBinding systemBaseBinding;
    private VerticalTabLayout tabLayout;
    private NoSwipeViewPager viewPager;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mFragmentList;

    @Override
    public void onInit() {
        ((TextView)findViewById(R.id.tv_title)).setText("系统设置");
        findViewById(R.id.rl_left_back).setOnClickListener(v -> finish());
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);
        mTitleList = new ArrayList<>();
        mTitleList.add("基本设置");
        mTitleList.add("高级设置");
        mTitleList.add("时间设置");
        mTitleList.add("网络设置");
        mTitleList.add("显示设置");
//        mTitleList.add("用户管理");
        mTitleList.add("退出登录");
//        mTitleList.add("系统升级");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new BaseSettingFragment());
        mFragmentList.add(new AdvancedSettingFragment());
        mFragmentList.add(new DateSettingFragment());
        mFragmentList.add(new NetworkSettingFragment());
        mFragmentList.add(new DisplaySettingFragment());
//        mFragmentList.add(new UserManageFragment());
        mFragmentList.add(new LogoutSettingFragment());
//        mFragmentList.add(new UpdataFragment());
        viewPager.setAdapter(new DataServicePageAdapter(mFragmentList,getSupportFragmentManager()));
    }

    private void initEvent(){

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setTabSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    private void initData(){
        tabLayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return mTitleList.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                QTabView.TabTitle title = new QTabView.TabTitle.Builder()
                        .setContent(mTitleList.get(position))
                        .setTextColor(getResources().getColor(R.color.white),getResources().getColor(R.color._333333))
                        .setTextSize(10)
                        .build();
                return title;
            }

            @Override
            public int getBackground(int position) {
                return R.drawable.bg_data_service_item;
            }
        });
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        systemBaseBinding = DataBindingUtil.setContentView(this, R.layout.act_system_setting);
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (!Settings.System.canWrite(this)){
                Toast.makeText(this,"系统未授权，将无法进行亮度、音量等调节操作，请授权后再进行操作。",Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}
