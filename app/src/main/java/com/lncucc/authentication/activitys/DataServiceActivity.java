package com.lncucc.authentication.activitys;

import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.receiver.UsbStateChangeReceiver;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataServicePageAdapter;
import com.lncucc.authentication.databinding.ActDataServiceBinding;
import com.lncucc.authentication.fragments.DataClearFragment;
import com.lncucc.authentication.fragments.DataImportFragment;
import com.lncucc.authentication.fragments.DataViewFragment;
import com.lncucc.authentication.fragments.DataExportFragment;

import java.util.ArrayList;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

import static com.askia.coremodel.rtc.Constants.ACTION_USB_PERMISSION;

/**
 * 数据服务
 */
@Route(path = ARouterPath.DATA_SERVICE_ACTIVITY)
public class DataServiceActivity extends BaseActivity {

    private ActDataServiceBinding dataBinding;
    private VerticalTabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<String> mTitleList;
    private ArrayList<Fragment> mFragmentList;

    @Override
    public void onInit() {
        ((TextView)findViewById(R.id.tv_title)).setText("数据服务");
        findViewById(R.id.rl_left_back).setOnClickListener(v -> finish());
        registerUDiskReceiver();
        initView();
        initData();
        initEvent();
    }

    private void initView(){
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        mTitleList = new ArrayList<>();
        mTitleList.add("数据查看");
        mTitleList.add("数据导入");
        mTitleList.add("数据导出");
        mTitleList.add("数据清空");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new DataViewFragment());
        mFragmentList.add(new DataImportFragment());
        mFragmentList.add(new DataExportFragment());
        mFragmentList.add(new DataClearFragment());

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

    /**
     * usb插拔广播 注册
     */
    private void registerUDiskReceiver() {
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        usbDeviceStateFilter.addAction("android.hardware.usb.action.USB_STATE");

        usbDeviceStateFilter.addAction(ACTION_USB_PERMISSION); //自定义广播

        registerReceiver(new UsbStateChangeReceiver(), usbDeviceStateFilter);

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
        dataBinding = DataBindingUtil.setContentView(this, R.layout.act_data_service);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
