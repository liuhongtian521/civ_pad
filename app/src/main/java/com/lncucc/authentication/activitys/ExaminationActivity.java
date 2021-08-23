package com.lncucc.authentication.activitys;


import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.viewmodel.ExaminationViewModel;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataServicePageAdapter;
import com.lncucc.authentication.databinding.ActExaminationBinding;
import com.lncucc.authentication.fragments.DataValidationFragment;
import com.lncucc.authentication.fragments.SessionFragment;
import com.lncucc.authentication.widgets.NoSwipeViewPager;

import java.util.ArrayList;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * 考试管理
 */
@Route(path = ARouterPath.EXAMINIATION_ACTIVITY)
public class ExaminationActivity extends BaseActivity {
    private ActExaminationBinding mExaminationBinding;
    private ExaminationViewModel mExaminationViewModel;
    private VerticalTabLayout tabLayout;
    private RelativeLayout back;
    private NoSwipeViewPager viewPager;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mFragmentList;

    @Override
    public void onInit() {
        initView();
        initData();
        initEvent();
    }

    @Override
    public void onInitViewModel() {
        mExaminationViewModel = ViewModelProviders.of(this).get(ExaminationViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        mExaminationBinding = DataBindingUtil.setContentView(this, R.layout.act_examination);
        mExaminationBinding.setViewmodel(mExaminationViewModel);
        mExaminationBinding.setOnclick(new ProxyClick());
    }

    private void initView(){
        back = findViewById(R.id.rl_left_back);
        back.setOnClickListener(v->finish());
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);
        mTitleList = new ArrayList<>();
        mTitleList.add("场次查看");
        mTitleList.add("验证数据查看");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new SessionFragment());
        mFragmentList.add(new DataValidationFragment());

        viewPager.setAdapter(new DataServicePageAdapter(mFragmentList,getSupportFragmentManager()));
        viewPager.setHorizontalScrollBarEnabled(false);

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
    public void onSubscribeViewModel() {

    }

    public class ProxyClick {
        //场次查看
        public void handleExaminationData(){

        }

        //验证数据查看
        public void handleExaminationNow(){

        }
    }
}
