//package com.lncucc.authentication.widgets
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.widget.EditText
//import com.askia.common.util.MyToastUtils
//import com.askia.coremodel.rtc.Constants
//import com.baidu.tts.tools.SharedPreferencesUtils
//import com.blankj.utilcode.util.LogUtils
//import com.lncucc.authentication.R
//
///**
// *Created by ymy
// *Description：
// *Date:2022/1/13 1:33 下午
// */
//
//class IPSettingDialog(context: Context?, listener: DialogClickBackListener) :
//    BaseDialog(context!!, R.style.DialogTheme) {
//    private val mView: View = LayoutInflater.from(context).inflate(R.layout.dialog_ip_setting, null)
//    private val mListener: DialogClickBackListener
//    private fun initEvent() {
//
//        val ip = (mView.findViewById<View>(R.id.edt_ip_input) as EditText).text.toString()
//        LogUtils.e("ip->${ip}")
//        mView.findViewById<View>(R.id.rl_close).setOnClickListener { dismiss() }
//        //cancel
//        mView.findViewById<View>(R.id.tv_cancel).setOnClickListener { dismiss() }
//        //confirm
//        mView.findViewById<View>(R.id.tv_confirm).setOnClickListener {
//
//            if (ip.isEmpty()) {
//                MyToastUtils.info("请输入IP地址")
//                return@setOnClickListener
//            }
//            if (!ip.contains(".")) {
//                MyToastUtils.info("IP地址格式输入错误")
//                return@setOnClickListener
//            }
//            //ip地址保存
//            SharedPreferencesUtils.putString(context, Constants.AUTO_BASE_URL, ip)
//            mListener.backType(
//                0
//            )
//        }
//    }
//
//    init {
//        setContentView(mView)
//        mListener = listener
//        initEvent()
//    }
//}