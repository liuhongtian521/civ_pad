package com.lncucc.authentication.activitys;

import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataServicePageAdapter;
import com.lncucc.authentication.databinding.ActLogsBinding;
import com.lncucc.authentication.fragments.DataClearFragment;
import com.lncucc.authentication.fragments.DataExportFragment;
import com.lncucc.authentication.fragments.DataImportFragment;
import com.lncucc.authentication.fragments.DataViewFragment;
import com.lncucc.authentication.fragments.LogsClearFragment;
import com.lncucc.authentication.fragments.LogsInfoFragment;
import com.lncucc.authentication.widgets.NoSwipeViewPager;

import java.util.ArrayList;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * 日志信息
 */
@Route(path = ARouterPath.LOGS_ACTIVITY)
public class LogActivity extends BaseActivity {
    private ActLogsBinding logsBinding;
    private VerticalTabLayout tabLayout;
    private NoSwipeViewPager viewPager;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mFragmentList;

    @Override
    public void onInit() {
        ((TextView)findViewById(R.id.tv_title)).setText("日志信息");
        findViewById(R.id.rl_left_back).setOnClickListener(v -> finish());
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
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

    private void initData() {
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

    private void initView() {
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);
        mTitleList = new ArrayList<>();
        mTitleList.add("日志信息");
        mTitleList.add("清空日志");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new LogsInfoFragment());
        mFragmentList.add(new LogsClearFragment());

        viewPager.setAdapter(new DataServicePageAdapter(mFragmentList,getSupportFragmentManager()));
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        logsBinding = DataBindingUtil.setContentView(this, R.layout.act_logs);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
