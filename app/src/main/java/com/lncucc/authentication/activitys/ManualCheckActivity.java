package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ImageUtil;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.data.KeyBoardBean;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.util.AssetsUtil;
import com.askia.coremodel.util.JsonUtil;
import com.askia.coremodel.util.Utils;
import com.askia.coremodel.viewmodel.AuthenticationViewModel;
import com.askia.coremodel.viewmodel.ManualCheckViewModel;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.KeyBoardAdapter;
import com.lncucc.authentication.databinding.ActManualCheckBinding;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.FaceComparedDialog;

import java.io.File;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;


/**
 * 人工审核
 * <p>
 * 新增准考证/身份证后4-6位模糊查询
 * -> 输入不足6位查询到多条不提示
 * 输入6位后如查询记录大于1条进行提示"查询到多条记录，请换准考证号/身份证号查询"
 * 新增拍照模块
 * -> 人工审核自动调用后置摄像头进行拍照后自动关闭
 * 新增键盘
 * -> 新增自定义键盘
 */
@Route(path = ARouterPath.MANUAL_CHECK)
public class ManualCheckActivity extends BaseActivity {

    private ManualCheckViewModel manualViewModel;
    private AuthenticationViewModel authViewModel;
    private ActManualCheckBinding manualBinding;
    //搜索类别 0 准考证查询 1 身份证查询
    private int searchType = 0;
    private KeyBoardAdapter keyBoardAdapter;
    private String examCode, seCode;
    private final static int PHOTO_REQUEST = 1001;
    private DBExamLayout examLayout;
    private FaceComparedDialog faceComparedDialog;
    private String similarity = "";
    private String path;

    @Override
    public void onInit() {
        findViewById(R.id.rl_left_back).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.tv_title)).setText("人工审核");

        //获取examCode,seCode
        examCode = getIntent().getStringExtra("examCode");
        seCode = getIntent().getStringExtra("seCode");
        initKeyView();
        initEvent();

        faceComparedDialog = new FaceComparedDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                faceComparedDialog.dismiss();
            }

            @Override
            public void backType(int type) {
                faceComparedDialog.dismiss();
                if (type == 0) {
                    //不通过
                    authViewModel.setMsg(examLayout, System.currentTimeMillis() + "", "2", similarity,"1");
                } else if (type == 1) {
                    //存疑
                    authViewModel.setMsg(examLayout, System.currentTimeMillis() + "", "3", similarity,"1");
                } else {
                    //通过
                    authViewModel.setMsg(examLayout, System.currentTimeMillis() + "", "1", similarity,"1");
                }
                MyToastUtils.success("审核成功！",Toast.LENGTH_SHORT);
            }
        });
    }

    /**
     * 初始化键盘
     */
    private void initKeyView() {
        String data = AssetsUtil.getJson(this, "keyboard.json");
        KeyBoardBean keyBoardBean = JsonUtil.Str2JsonBean(data, KeyBoardBean.class);
        keyBoardAdapter = new KeyBoardAdapter(keyBoardBean.getData());
        manualBinding.rlKeyboard.setLayoutManager(new GridLayoutManager(this, 3));
        manualBinding.rlKeyboard.setAdapter(keyBoardAdapter);

    }

    @Override
    public void onInitViewModel() {
        manualViewModel = ViewModelProviders.of(this).get(ManualCheckViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
    }

    private void initEvent() {
        //点击准考证查询
        manualBinding.relLeft.setOnClickListener(v -> {
            searchType = 0;
            manualBinding.editExamNumber.setText("");
            manualBinding.tvLeft.setTextAppearance(ManualCheckActivity.this, R.style.searchSelected);
            manualBinding.tvRight.setTextAppearance(ManualCheckActivity.this, R.style.searchUnSelected);
            manualBinding.ivLeft.setVisibility(View.VISIBLE);
            manualBinding.ivRight.setVisibility(View.GONE);
        });
        //点击身份证查询
        manualBinding.relRight.setOnClickListener(v -> {
            searchType = 1;
            manualBinding.editExamNumber.setText("");
            manualBinding.tvLeft.setTextAppearance(ManualCheckActivity.this, R.style.searchUnSelected);
            manualBinding.tvRight.setTextAppearance(ManualCheckActivity.this, R.style.searchSelected);
            manualBinding.ivLeft.setVisibility(View.GONE);
            manualBinding.ivRight.setVisibility(View.VISIBLE);
        });

        //键盘输入事件
        keyBoardAdapter.setOnItemClickListener((adapter, view, position) -> {

            String condition = manualBinding.editExamNumber.getText().toString();
            StringBuilder buffer = new StringBuilder(condition);
            switch (position) {
                case 0:
                    buffer.append("1");
                    break;
                case 1:
                    buffer.append("2");
                    break;
                case 2:
                    buffer.append("3");
                    break;
                case 3:
                    buffer.append("4");
                    break;
                case 4:
                    buffer.append("5");
                    break;
                case 5:
                    buffer.append("6");
                    break;
                case 6:
                    buffer.append("7");
                    break;
                case 7:
                    buffer.append("8");
                    break;
                case 8:
                    buffer.append("9");
                    break;
                case 9:
                    buffer.append("X");
                    break;
                case 10:
                    buffer.append("0");
                    break;
                case 11:
                    if (buffer.length() > 0) {
                        //删除最后字符
                        buffer.delete(buffer.length() - 1, buffer.length());
                    }
                    break;
            }

            //更新键盘输入结果
            condition = buffer.toString();
            manualBinding.editExamNumber.setText(condition);
        });

        //搜索条件清空
        manualBinding.btnSearch.setOnClickListener(v -> {
            //清除搜索条件
            manualBinding.editExamNumber.setText("");
            //隐藏学生信息模块展示空白占位区域
            manualBinding.linePeople.setVisibility(View.GONE);
            manualBinding.rlBlank.setVisibility(View.VISIBLE);
        });
        //监听搜索条件输入
        manualBinding.editExamNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //搜索条件长度>4 自动进行搜索
                int length = s.length();
                if (length >= 4) {
                    //如果长度》=4 直接搜索
                    manualViewModel.fetchStudentInfo(searchType, s.toString(), examCode, seCode);
                }
            }
        });

        manualBinding.btnNext.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("stuNo",examLayout.getStuNo());
            bundle.putString("seCode",examLayout.getSeCode());
            bundle.putString("path", path);
            if (!Utils.doubleClick()){
                //点击比对
                startActivityForResultByRouter(ARouterPath.TAKE_PHOTO,PHOTO_REQUEST,bundle);
            }
        });
    }

    @Override
    public void onInitDataBinding() {
        manualBinding = DataBindingUtil.setContentView(this, R.layout.act_manual_check);
    }

    @Override
    public void onSubscribeViewModel() {
        //搜索结果回调,展示学生信息
        manualViewModel.getStudentInfo().observe(this, result -> {
            if (result != null) {
                //160349
                examLayout = result;
                manualBinding.linePeople.setVisibility(View.VISIBLE);
                manualBinding.rlBlank.setVisibility(View.GONE);
                manualBinding.tvPeopleName.setText(result.getStuName());
                manualBinding.tvCardno.setText(result.getExReNum());
                manualBinding.tvIdCard.setText(result.getIdCard());
                manualBinding.tvAddress.setText(result.getRoomNo());
                manualBinding.tvAddressNo.setText(result.getSeatNo());
                String healthStatus = "";
                switch (result.getHealthCode()){
                    case "0":
                        healthStatus = "绿码";
                        break;
                    case "1":
                        healthStatus = "黄码";
                        break;
                    case "2":
                        healthStatus = "红码";
                        break;
                    case "3":
                    case "":
                        healthStatus = "未知";
                        break;
                }
                manualBinding.tvHealthCode.setText(healthStatus);

                path = UN_ZIP_PATH + File.separator + examCode + File.separator + "photo" + File.separator + result.getStuNo() + ".jpg";
                //转换file
                File file = new File(path);
                if (file.exists()) {
                    if (manualBinding.ivFace.getVisibility() != View.VISIBLE)
                        manualBinding.ivFace.setVisibility(View.VISIBLE);
                    //转换bitmap
                    Bitmap bt = ImageUtil.getRotateNewBitmap(path);
                    manualBinding.ivFace.setImageBitmap(bt);
                } else {
                    manualBinding.ivFace.setVisibility(View.INVISIBLE);
                }
            }else {
                //搜索数据为空，隐藏学生信息展示占位图
                manualBinding.linePeople.setVisibility(View.GONE);
                manualBinding.rlBlank.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //自动拍照存储成功回调此处显示比对dialog,只有自动关闭后回调
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK){
            if (data != null){
                similarity = data.getStringExtra("similarity");
            }
            faceComparedDialog.setSate(examLayout);
            faceComparedDialog.setSuccess(true);
            faceComparedDialog.setNumber(similarity);
            //隐藏关闭按钮
            faceComparedDialog.setButtonGone();
            faceComparedDialog.show();
        }
        //手动取消人脸比对
        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_CANCELED){
            MyToastUtils.error("取消比对", Toast.LENGTH_SHORT);
        }
    }
}
