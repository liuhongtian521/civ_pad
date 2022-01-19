package com.lncucc.authentication.fragments;

import static com.askia.coremodel.rtc.Constants.AUTO_BASE_URL;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.askia.coremodel.datamodel.http.ApiConstants;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentNetworkSettingBinding;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.IPSettingDialog;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导入
 */
public class NetworkSettingFragment extends BaseFragment implements DialogClickBackListener {
    private FragmentNetworkSettingBinding networkSetting;
    Intent intent = null;
    private IPSettingDialog dialog;

    @Override
    public void onInit() {
        networkSetting.netSetBtn.setOnClickListener(v -> openNetwork());
        dialog = new IPSettingDialog(getActivity(), this);
        //ipDialog 展示
        networkSetting.tvRequestAddress.setOnClickListener(v -> dialog.show());
        setHostIp();
    }

    @Override
    public void onInitViewModel() {

    }

    private void setHostIp(){
        String url = SharedPreferencesUtils.getString(getActivity(),AUTO_BASE_URL);
        if (url == null){
            url = ApiConstants.HOST;
        }
        Uri uri = Uri.parse(url);
        String host = uri.getHost();
        String[] ipArray = host.split("\\.");
        if (ipArray.length == 4) {
            networkSetting.ipInput4.setText(ipArray[0]);
            networkSetting.ipInput3.setText(ipArray[1]);
            networkSetting.ipInput2.setText(ipArray[2]);
            networkSetting.ipInput1.setText(ipArray[3]);
        }
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        networkSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_network_setting, container, false);
        return networkSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public void openNetwork() {
        intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void dissMiss() {
        KeyboardUtils.toggleSoftInput();
    }

    @Override
    public void backType(int type) {
        if (type == 0 && dialog != null && dialog.isShowing()) {
            setHostIp();
            //更新本地IP展示
            dialog.dismiss();
            KeyboardUtils.toggleSoftInput();
        }
    }
}
