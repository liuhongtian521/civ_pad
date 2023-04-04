package com.lncucc.authentication.widgets;

import static com.askia.coremodel.rtc.Constants.AUTO_BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.utils.TextUtils;
import com.askia.common.base.ViewManager;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.rtc.Constants;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.lncucc.authentication.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

/**
 * Created by ymy
 * Description：
 * Date:2022/1/13 2:39 下午
 */
public class IPSettingDialog extends BaseDialog {

    private View mView;
    private DialogClickBackListener callback;
    private EditText editText;

    //private TextView defaultIp;
    public IPSettingDialog(Context context, DialogClickBackListener listener) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_ip_setting, null);
        this.callback = listener;
        setContentView(mView);
        setCanceledOnTouchOutside(true);
        editText = mView.findViewById(R.id.edt_ip_input);
        String url =  SharedPreferencesUtils.getString(ViewManager.getInstance().currentActivity(),AUTO_BASE_URL);
        if(null!=url){
            editText.setText(url.replaceAll("http://",""));
        }
        mView.findViewById(R.id.rl_close).setOnClickListener(v -> {
            KeyboardUtils.toggleSoftInput();
            dismiss();
        });
        //cancel
        mView.findViewById(R.id.tv_cancel).setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(getWindow().getDecorView());
            dismiss();
        });
        //confirm
        mView.findViewById(R.id.tv_confirm).setOnClickListener(v -> {
            String ip = editText.getText().toString();
            if (TextUtils.isEmpty(ip)) {
                MyToastUtils.error("请输入IP地址", Toast.LENGTH_SHORT);
            } else {
                Pattern p = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");
                Matcher m = p.matcher(ip);
                if (m.matches()){
                    String baseUrl = "http://" + ip;
                    SharedPreferencesUtils.putString(context, Constants.AUTO_BASE_URL, baseUrl);
                    MyToastUtils.success("IP修改成功",0);
                    //切换baseUrl
                    RetrofitUrlManager.getInstance().setGlobalDomain(baseUrl);
                    RetrofitUrlManager.getInstance().putDomain("baseDomain",baseUrl);
                    callback.backType(0);
                    KeyboardUtils.toggleSoftInput();
                }else {
                    MyToastUtils.error("IP输入错误，请重新输入",0);
                }
            }
        });
    }
}
