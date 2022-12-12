package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.recyclerview.FOnRVItemClickListener;
import com.askia.common.recyclerview.FRecyclerViewAdapter;
import com.askia.common.recyclerview.FViewHolderHelper;
import com.askia.common.util.ImageUtil;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.util.Utils;
import com.askia.coremodel.viewmodel.AuthenticationViewModel;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActAuthenticationBinding;
import com.lncucc.authentication.fragments.FaceShowFragment;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.FaceComparedDialog;
import com.lncucc.authentication.widgets.FaceResultDialog;
import com.lncucc.authentication.widgets.InquiryDialog;
import com.lncucc.authentication.widgets.PeopleMsgDialog;
import com.lncucc.authentication.widgets.PopExamPlan;
import com.unicom.facedetect.detect.FaceDetectResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;
import static com.lncucc.authentication.fragments.DataExportFragment.FULL_SCREEN_FLAG;

/**
 * Create bt she:
 * 身份验证
 *
 * @date 2021/8/5
 */
@Route(path = ARouterPath.IDENTIFY_ACTIVITY)
public class AuthenticationActivity extends BaseActivity {
    private ActAuthenticationBinding mDataBinding;

    private AuthenticationViewModel mViewModel;
    //考生信息 dialog
    private PeopleMsgDialog peopleMsgDialog;
    //刷脸结果
    private FaceResultDialog faceResultDialog;
    //搜索学生
//    private InquiryDialog inquiryDialog;
    //对比结果
    private FaceComparedDialog faceComparedDialog;
    //选择考试计划
    private PopExamPlan mPopExamPlan;
    //右侧列表适配器
    private FRecyclerViewAdapter<DBExamExport> mAdapter;
    //存储list
    private List<DBExamExport> saveList;
    private FaceShowFragment faceFragment;
    private FaceDetectResult mDetectResult;
    private DBExaminee mDbExaminee;

    private String stuNo;
    private String base64;
    private DBExamLayout mDbExamLayout;
    private String mSeCode = "changcima01";//场次码
    private String mExamCode = "examCode01";
    private boolean isComparison = false;
    long timeStart, timeEnd;

    int mStudentNumber = 0;
    //屏幕旋转处理
    int showTime = 0;
    boolean isToUp = true;


    private ArrayList<String> mExamCodeList = new ArrayList<>();
//    private List<DBExamArrange> mExamArrangeList = new ArrayList<>();

    public String getmSeCode() {
        return mSeCode;
    }

    public boolean isComparison() {
        return isComparison;
    }

    public String getStuNo() {
        return stuNo;
    }

    private int TIME = 1000;  //每隔1s执行一次.
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                timer();
                handler.postDelayed(this, TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void timer() {
        long nowTime = System.currentTimeMillis();
        mDataBinding.tvTime.setText(TimeUtils.millis2String(nowTime));
        if (nowTime > timeEnd) {
            //关闭dialog
            if (faceResultDialog != null && faceResultDialog.isShowing()) {
                faceResultDialog.dismiss();
            }
            if (peopleMsgDialog != null && peopleMsgDialog.isShowing()) {
                peopleMsgDialog.dismiss();
            }
            if (faceComparedDialog != null && faceComparedDialog.isShowing()) {
                faceComparedDialog.dismiss();
            }
            startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
            finish();
        }
    }

    //相机翻转
    public void setShowTime(int orientation) {
        if (peopleMsgDialog.isShowing() || faceResultDialog.isShowing() || faceComparedDialog.isShowing() || mPopExamPlan.isShowing())
            return;

        if (orientation >= 270) {        //翻转 向上
            if (!isToUp) {
                isToUp = true;
                faceFragment.setToUp(1);
            }
        } else if (orientation < 80) {            //反转向下
            if (isToUp) {
                isToUp = false;
                faceFragment.setToUp(3);
            }
        }
    }


    //修改secode
    public void setmSeCode(String seCode) {
        if (seCode == null)
            return;
        mSeCode = seCode;
        if (faceFragment != null)
            faceFragment.setmSeCode(seCode);
    }

    //修改examcode
    public void semExamCode(String mExamCode) {
        if (mExamCode == null)
            return;
        Log.e("TagSnake Auth", mExamCode);
        this.mExamCode = mExamCode;
        //获取场次数据
        mViewModel.getPlanByCode(mExamCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        semExamCode(this.mExamCode);
    }

    @Override
    public void onInit() {
        KeyboardUtils.hideSoftInput(this);
        faceFragment = (FaceShowFragment) getFragment(ARouterPath.FACE_SHOW_ACTIVITY);
        addFragment(faceFragment, R.id.frame_layout);
        if (getIntent().getExtras() != null) {
            semExamCode(getIntent().getExtras().getString("mExamCode"));
            mViewModel.getPlane(mExamCode);
            mExamCodeList = getIntent().getExtras().getStringArrayList("list");

            timeStart = getIntent().getExtras().getLong("startTIME");
            timeEnd = getIntent().getExtras().getLong("endTIME");
        }
        if (mExamCodeList == null) {
            mExamCodeList = new ArrayList<>();
        } else
            mDataBinding.tvSessionAll.setText(mExamCodeList.size() + "");

        saveList = new ArrayList<>();


        //记录列表展示
        mAdapter = new FRecyclerViewAdapter<DBExamExport>(mDataBinding.rvList, R.layout.item_verify) {
            @Override
            protected void fillData(FViewHolderHelper viewHolderHelper, int position, DBExamExport model) {
                if (model == null) {
                    return;
                }
                viewHolderHelper.setText(R.id.tv_item_verify_name, model.getStuName());
                if ("1".equals(model.getVerifyResult())) {
                    viewHolderHelper.getImageView(R.id.iv_type).setImageResource(R.mipmap.icon_type_success);
                } else if ("2".equals(model.getVerifyResult())) {
                    viewHolderHelper.getImageView(R.id.iv_type).setImageResource(R.mipmap.icon_type_faile);
                } else {
                    viewHolderHelper.getImageView(R.id.iv_type).setImageResource(R.mipmap.icon_cunyi);

                }
                String path = UN_ZIP_PATH + File.separator + mExamCode + "/photo/" + model.getStuNo() + ".jpg";
                //转换file
                File file = new File(path);
                if (file.exists()) {
                    if (viewHolderHelper.getView(R.id.iv_item_head_one).getVisibility() != View.VISIBLE)
                        viewHolderHelper.getView(R.id.iv_item_head_one).setVisibility(View.VISIBLE);
                    //转换bitmap
                    Bitmap bt = ImageUtil.getRotateNewBitmap(path);
                    viewHolderHelper.setImageBitmap(R.id.iv_item_head_one, bt);
                } else {
                    viewHolderHelper.getView(R.id.iv_item_head_one).setVisibility(View.INVISIBLE);
                }

                String pathT = Constants.STU_EXPORT + File.separator + mSeCode + File.separator + "photo" + File.separator + model.getStuNo() + ".jpg";
                File file1 = new File(pathT);
                if (file1.exists()) {
                    //转换bitmap
                    try {
                        FileInputStream fiss = new FileInputStream(file1);
                        Bitmap bt = BitmapFactory.decodeStream(fiss);
//                    Bitmap bts =BitmapFactory.decodeStream(getClass().getResourceAsStream(path));
                        viewHolderHelper.setImageBitmap(R.id.iv_item_head_two, bt);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        mAdapter.setOnRVItemClickListener(new FOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                faceFragment.closeFace();
                peopleMsgDialog.show();
                peopleMsgDialog.setMsg(saveList.get(position));
            }
        });

        mAdapter.setData(saveList);
        mAdapter.setListLength(6);//列表最多只显示六条
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataBinding.rvList.setLayoutManager(linearLayoutManager);
        mDataBinding.rvList.setAdapter(mAdapter);

        peopleMsgDialog = new PeopleMsgDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                peopleMsgDialog.dismiss();
                faceFragment.goContinueDetectFace();
            }

            @Override
            public void backType(int type) {
                peopleMsgDialog.dismiss();
                faceFragment.goContinueDetectFace();
            }
        });


        faceResultDialog = new FaceResultDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                faceResultDialog.dismiss();
                if (!mPopExamPlan.isShowing() && !peopleMsgDialog.isShowing() && !faceComparedDialog.isShowing())
                    faceFragment.goContinueDetectFace();
            }

            @Override
            public void backType(int type) {
                faceResultDialog.dismiss();
                if (!mPopExamPlan.isShowing() && !peopleMsgDialog.isShowing() && !faceComparedDialog.isShowing())
                    faceFragment.goContinueDetectFace();
                if (type == 0) {
                    //不通过
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "2", mDetectResult == null ? "0.00" : Float.toString(mDetectResult.similarity), "0");
                } else if (type == 1) {
                    //存疑
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "3", mDetectResult == null ? "0.00" : Float.toString(mDetectResult.similarity), "0");
                } else {
                    //通过
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "1", mDetectResult == null ? "0.00" : Float.toString(mDetectResult.similarity), "0");
                }
            }
        });

        faceComparedDialog = new FaceComparedDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                isComparison = false;
                faceComparedDialog.dismiss();
                faceFragment.goContinueDetectFace();
            }

            @Override
            public void backType(int type) {
                isComparison = false;
                faceComparedDialog.dismiss();
                faceFragment.goContinueDetectFace();
                if (type == 0) {
                    //不通过
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "2", mDetectResult == null ? "0.00" : Float.toString(mDetectResult.similarity), "0");
                } else if (type == 1) {
                    //存疑
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "3", mDetectResult == null ? "0.00" : Float.toString(mDetectResult.similarity), "0");
                } else {
                    //通过
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "1", mDetectResult == null ? "0.00" : Float.toString(mDetectResult.similarity), "0");
                }
            }
        });

        mPopExamPlan = new PopExamPlan(this, new PopExamPlan.PopListener() {
            @Override
            public void close(DBExamPlan dbExamPlan) {
                mPopExamPlan.dismiss();
                semExamCode(dbExamPlan.getExamCode());
                mDataBinding.tvExamname.setText(dbExamPlan.getExamName());
            }
        });

        mPopExamPlan.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_blue_arrow_down);
                isComparison = false;

            }
        });

        mDataBinding.lineChooseExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceFragment.closeFace();
                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_blue_arrow_up);
                mPopExamPlan.setIndex(mExamCode);
                mPopExamPlan.setFocusable(false);
                mPopExamPlan.showAtLocation(mDataBinding.bgMain, Gravity.CENTER, 0, 0);
                mPopExamPlan.getContentView().setSystemUiVisibility(FULL_SCREEN_FLAG);
                mPopExamPlan.setFocusable(true);
                mPopExamPlan.update();
            }
        });
        handler.postDelayed(runnable, TIME);
    }

    OrientationEventListener mOrientationEventListener;

    @Override
    protected void onResume() {
        super.onResume();
        if (mOrientationEventListener == null)
            mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int orientation) {
                    //屏幕角度监听
//                    setShowTime(orientation);
                }
            };

        if (mOrientationEventListener.canDetectOrientation())
            mOrientationEventListener.enable();
        else
            mOrientationEventListener.disable();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        faceFragment.startCamera();
        faceFragment.goContinueDetectFace();
        handler.postDelayed(runnable, TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        faceFragment.closeFace();
        faceFragment.releaseCamera();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {

        super.onPause();
        KeyboardUtils.hideSoftInput(this);
        mOrientationEventListener.disable();
        faceFragment.closeFace();
        faceFragment.releaseCamera();
        if (faceComparedDialog.isShowing())
            faceComparedDialog.dismiss();
        if (faceResultDialog.isShowing())
            faceResultDialog.dismiss();
        if (peopleMsgDialog.isShowing())
            peopleMsgDialog.dismiss();
    }

    @Override
    public void onInitViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AuthenticationViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_authentication);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

        //考试名字
        mViewModel.getmDBExamPlan().observe(this, new Observer<DBExamPlan>() {
            @Override
            public void onChanged(DBExamPlan dbExamPlan) {
                mDataBinding.tvExamname.setText(dbExamPlan.getExamName());
            }
        });

        //判断是否可以刷脸
        mViewModel.getmCanSign().observe(this, (Observer<String>) result -> {
            Log.e("当前健康码状态->", result);
            if (mPopExamPlan.isShowing() || peopleMsgDialog.isShowing() || faceResultDialog.isShowing() || faceComparedDialog.isShowing())
                return;
            //刷脸成功
            faceResultDialog.setType(true, result);
        });


        //刷脸记录
        mViewModel.getmDBExamExport().observe(this, new Observer<DBExamExport>() {
            @Override
            public void onChanged(DBExamExport dbExamExport) {
                boolean have = false;
                int haveIndex = -1;
                for (int index = 0; index < saveList.size(); index++) {
                    if (saveList.get(index).getStuNo().equals(dbExamExport.getStuNo())) {
                        have = true;
                        haveIndex = index;
                        break;
                    }
                }
                if (have) {
                    Log.e("TagSnake", "havethis" + haveIndex);
                    //移除旧的验证数据，添加新的验证数据。
                    saveList.remove(haveIndex);
                    saveList.add(0, dbExamExport);
                    //新增排序


                } else {
                    saveList.add(0, dbExamExport);
                }
                mViewModel.getExamNumber(mSeCode, mExamCode);
//                mDataBinding.tvVerifyNumber.setText(saveList.size() + "/" + mStudentNumber);
                mAdapter.notifyDataSetChanged();
            }
        });

        //场次数据
        mViewModel.getmDBExamArrange().observe(this, new Observer<DBExamArrange>() {
            @Override
            public void onChanged(DBExamArrange dbExamArrange) {
                if (dbExamArrange == null) {
                    Toast.makeText(getApplicationContext(), "当前考试计划没有正在进行中的场次", Toast.LENGTH_SHORT).show();
                    Bundle _d = new Bundle();
                    _d.putString("mExamCode", mExamCode);
                    startActivityByRouter(ARouterPath.MAIN_ACTIVITY, _d);
                    finish();
                    return;
                }
                setmSeCode(dbExamArrange.getSeCode());
                mDataBinding.tvSession.setText(dbExamArrange.getSeName());
                //考场总数
                mDataBinding.tvSessionAll.setText(DBOperation.getRoomList(dbExamArrange.getSeCode()).size() + "");
                mStudentNumber = DBOperation.getStudentNumber(mExamCode, mSeCode);
                mViewModel.getExamNumber(mSeCode, mExamCode);
//                mDataBinding.tvVerifyNumber.setText("0/" + mStudentNumber);
                mViewModel.getExamNumber(dbExamArrange.getSeCode(), dbExamArrange.getExamCode());
                saveList.clear();
            }
        });
        //验证数量
        mViewModel.getmDBExamExportNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mDataBinding.tvVerifyNumber.setText(integer + "/" + mStudentNumber);
            }
        });

        //查询学生返回
        mViewModel.getmCheckVersionData().observe(this, new Observer<DBExaminee>() {
            @Override
            public void onChanged(DBExaminee dbExaminee) {
                if (mPopExamPlan.isShowing() || peopleMsgDialog.isShowing() || faceResultDialog.isShowing() || faceComparedDialog.isShowing())
                    return;
                if (dbExaminee == null) {
                    if (!isComparison) {
                        faceResultDialog.setType(false, "");
                    } else
                        faceFragment.goContinueDetectFace();
                } else {
                    mDbExaminee = dbExaminee;
                    if (!faceComparedDialog.getSuccess()) {
                        mViewModel.getSeatAbout(mDbExaminee.getStuNo(), mExamCode, mSeCode);
                    } else {
                        mViewModel.getSeatAbout(mDetectResult.faceNum, mExamCode, mSeCode);
                    }
                }
            }
        });

        //用户考场数据
        mViewModel.getmSeat().observe(this, new Observer<DBExamLayout>() {
            @Override
            public void onChanged(DBExamLayout dbExamLayout) {
                if (mPopExamPlan.isShowing() || peopleMsgDialog.isShowing() || faceResultDialog.isShowing() || faceComparedDialog.isShowing())
                    return;
                if (dbExamLayout != null) {
                    mDbExamLayout = dbExamLayout;
                    //比对状态
                    if (isComparison) {
                        faceComparedDialog.show();
                        //人员的考场和座位
                        faceComparedDialog.setSate(dbExamLayout);
                        if (!faceComparedDialog.getSuccess()) {
                            faceComparedDialog.setNumber("----");
                        } else {
                            faceComparedDialog.setNumber(Float.toString(mDetectResult.similarity));
                        }
                    } else {
                        //识别状态
                        if (mExamCodeList.size() == 0)
                            mViewModel.canSign(dbExamLayout.getId());
                        else if (mExamCodeList.indexOf(dbExamLayout.getRoomNo()) > -1) {
                            mViewModel.canSign(dbExamLayout.getId());
                        } else
                            faceResultDialog.setType(false, "");
                    }
                } else {
                    if (!isComparison) {
                        faceResultDialog.setType(false, "");
                    } else {
                        faceFragment.goContinueDetectFace();
                    }
                }
            }
        });

        //获取学生数据
        mViewModel.getmStudent().observe(this, new Observer<DBExamLayout>() {
            @Override
            public void onChanged(DBExamLayout dbExamLayout) {
                if (dbExamLayout != null) {
//                    inquiryDialog.setDbExamLayout(dbExamLayout);
                } else {
                    Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //选择考场
    public void chooseExamination(View view) {
        if (!Utils.doubleClick()) {
            Bundle _b = new Bundle();
            _b.putString("SE_CODE", mSeCode);
            startActivityForResultByRouter(ARouterPath.CHOOSE_VENVE, 1211, _b);
        }
    }

    public void setting(View view) {
        if (!Utils.doubleClick()) {
            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
        }
    }

    //人工审核
    public void audit(View view) {
        faceFragment.closeFace();
        if (!Utils.doubleClick()) {
            Bundle bundle = new Bundle();
            bundle.putString("examCode", mExamCode);
            bundle.putString("seCode", mSeCode);
            startActivityByRouter(ARouterPath.MANUAL_CHECK, bundle);
        }
    }


    public void toSeeMore(View view) {
        if (!Utils.doubleClick()) {
            Bundle bundle = new Bundle();
            bundle.putString("seCode", mSeCode);
            startActivityByRouter(ARouterPath.DATA_VIEW, bundle);
        }
    }

    //页面返回接收
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1211 && resultCode == 1) {
            mExamCodeList.clear();
            mExamCodeList.addAll(data.getStringArrayListExtra("list"));
            mDataBinding.tvSessionAll.setText(mExamCodeList.size() + "");
        }
    }

    /*这个是返回人脸数据和图片
     * */
    public void getFace(FaceDetectResult detectResult) {
        if (mPopExamPlan.isShowing() || peopleMsgDialog.isShowing() || faceResultDialog.isShowing() || faceComparedDialog.isShowing())
            return;
        if (isComparison) {
            if (detectResult.faceNum != null && mDbExaminee.getStuNo().equals(detectResult.faceNum)) {
                //对比数据成功
                this.mDetectResult = detectResult;
                faceComparedDialog.setSuccess(true);
                mViewModel.quickPeople(detectResult.faceNum, mExamCode);
            } else {//比对失败
                faceComparedDialog.setSuccess(false);
                mViewModel.quickPeople(mDbExaminee.getStuNo(), mExamCode);
            }
        } else {
            if (detectResult != null && detectResult.similarity > 0.85f) {
                this.mDetectResult = detectResult;
                mViewModel.quickPeople(mDetectResult.faceNum, mExamCode);//查询学生
            } else {
                faceResultDialog.setType(false, "");
            }
        }
    }

    private void fullScreenImmersive(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            view.setSystemUiVisibility(uiOptions);
        }
    }
}
