package com.lncucc.authentication.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.askia.coremodel.datamodel.http.ApiConstants;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentNetworkSettingBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导入
 */
public class NetworkSettingFragment extends BaseFragment {
    private FragmentNetworkSettingBinding networkSetting;
    TextView netVBtn;
    Intent intent=null;

    @Override
    public void onInit() {
    netVBtn = networkSetting.netSetBtn;
    netVBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            openNetwork();
            return;
        }
    });
        String ip = ApiConstants.HOST.substring(7,ApiConstants.HOST.length()-7);
        String ipArray[] = ip.split("\\.");
        networkSetting.ipInput4.setText(ipArray[0]);
        networkSetting.ipInput3.setText(ipArray[1]);
        networkSetting.ipInput2.setText(ipArray[2]);
        networkSetting.ipInput1.setText(ipArray[3]);
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        networkSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_network_setting,container,false);
        return networkSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public void openNetwork () {
        if(android.os.Build.VERSION.SDK_INT>10){
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }else{
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        startActivity(intent);
    }

}
