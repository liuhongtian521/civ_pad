package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.askia.common.recyclerview.FOnItemChildClickListener;
import com.askia.common.recyclerview.FOnRVItemClickListener;
import com.askia.common.recyclerview.FRecyclerViewAdapter;
import com.askia.common.recyclerview.FViewHolderHelper;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.viewmodel.AuthenticationViewModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;

/**
 * Create bt she:
 * 身份验证
 *
 * @date 2021/8/5
 */
@Route(path = ARouterPath.IDENTIFY_ACTIVITY)
public class AuthenticationActivity extends BaseActivity {
    private ActAuthenticationBinding mDataBinding;
    //ZhjyBU@202104

    private AuthenticationViewModel mViewModel;
    //考生信息 dialog
    private PeopleMsgDialog peopleMsgDialog;
    //刷脸结果
    private FaceResultDialog faceResultDialog;
    //搜索学生
    private InquiryDialog inquiryDialog;
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
    private String base64;
    private DBExamLayout mDbExamLayout;
    private String mSeCode = "changcima01";//场次码
    private String mExanCode = "examCode01";
    private boolean isComparison = false;
    long timeStart, timeEnd;

    int mStudentNumber = 0;


    private ArrayList<String> mExamCodeList = new ArrayList<>();
//    private List<DBExamArrange> mExamArrangeList = new ArrayList<>();

    public String getmSeCode() {
        return mSeCode;
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
            startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
            finish();
        }
    }


    public void setmSeCode(String seCode) {
        if (seCode == null)
            return;
        mSeCode = seCode;
        if (faceFragment != null)
            faceFragment.setmSeCode(seCode);
//        mViewModel.getSize(seCode);
    }

    public void semExanCode(String mExanCode) {
        if (mExanCode == null)
            return;
        this.mExanCode = mExanCode;
        //获取场次数据
        mViewModel.getPlanByCode(mExanCode);
    }


    @Override
    public void onInit() {
        faceFragment = (FaceShowFragment) getFragment(ARouterPath.FACE_SHOW_ACTIVITY);
        addFragment(faceFragment, R.id.frame_layout);
        semExanCode(getIntent().getExtras().getString("exanCode"));
        mExamCodeList = getIntent().getExtras().getStringArrayList("list");
        if (mExamCodeList == null) {
            mExamCodeList = new ArrayList<>();
        } else
            mDataBinding.tvSessionAll.setText(mExamCodeList.size() + "");

        timeStart = getIntent().getExtras().getLong("startTIME");
        timeEnd = getIntent().getExtras().getLong("endTIME");
        saveList = new ArrayList<>();

        mAdapter = new FRecyclerViewAdapter<DBExamExport>(mDataBinding.rvList, R.layout.item_verify) {
            @Override
            protected void fillData(FViewHolderHelper viewHolderHelper, int position, DBExamExport model) {
                if (model == null) {
                    return;
                }
                viewHolderHelper.setText(R.id.tv_item_verify_name, model.getStuName());
                Log.e("TagSnake itemType", model.getVerifyResult());
                if ("1".equals(model.getVerifyResult())) {
                    viewHolderHelper.getImageView(R.id.iv_type).setImageResource(R.drawable.icon_type_success);
                } else {
                    viewHolderHelper.getImageView(R.id.iv_type).setImageResource(R.drawable.icon_type_faile);
                }
                String path = UN_ZIP_PATH + File.separator + mExanCode + "/photo/" + model.getStuNo() + ".jpg";
                //转换file
                File file = new File(path);
                if (file.exists()) {
                    //转换bitmap
                    Bitmap bt = BitmapFactory.decodeFile(path);
                    viewHolderHelper.setImageBitmap(R.id.iv_item_head_one, bt);
                }

                String pathT = Constants.STU_EXPORT + File.separator + mSeCode + File.separator + "photo" + File.separator + model.getStuNo() + ".png";
//                Log.e("TagSnake itemT", pathT);

//                Log.e("TagSnake list", pathT);
                File file1 = new File(pathT);
                if (file1.exists()) {
                    //转换bitmap
                    Bitmap bt = BitmapFactory.decodeFile(pathT);
                    viewHolderHelper.setImageBitmap(R.id.iv_item_head_two, bt);
                }
            }
        };

        mAdapter.setOnRVItemClickListener(new FOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Log.e("TagSnake list", "item =" + position);
                faceFragment.closeFace();
                peopleMsgDialog.show();
                peopleMsgDialog.setMsg(saveList.get(position));
            }
        });

        mAdapter.setOnItemChildClickListener(new FOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                Log.e("TagSnake list", "item =" + position);
                peopleMsgDialog.show();
            }
        });

        mAdapter.setData(saveList);
        mAdapter.setListLength(6);
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
                faceFragment.goContinueDetectFace();
            }

            @Override
            public void backType(int type) {
                faceResultDialog.dismiss();


                faceFragment.goContinueDetectFace();
                if (type == 0) {
                    //不通过
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "0", Float.toString(mDetectResult.similarity), mDbExaminee.getId(), mDbExaminee.getCardNo());
                } else if (type == 1) {
                    //存疑
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "2", Float.toString(mDetectResult.similarity), mDbExaminee.getId(), mDbExaminee.getCardNo());
                } else {
                    //通过
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "1", Float.toString(mDetectResult.similarity), mDbExaminee.getId(), mDbExaminee.getCardNo());
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
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "0", Float.toString(mDetectResult.similarity), mDbExaminee.getId(), mDbExaminee.getCardNo());
                } else if (type == 1) {
                    //存疑
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "2", Float.toString(mDetectResult.similarity), mDbExaminee.getId(), mDbExaminee.getCardNo());
                } else {
                    //通过
                    mViewModel.setMsg(mDbExamLayout, System.currentTimeMillis() + "", "1", Float.toString(mDetectResult.similarity), mDbExaminee.getId(), mDbExaminee.getCardNo());
                }
            }
        });

        inquiryDialog = new InquiryDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                inquiryDialog.dismiss();
                faceFragment.goContinueDetectFace();
            }

            @Override
            public void backType(int type) {
                mDbExamLayout = inquiryDialog.getDbExamLayout();
                DBExaminee newDbExamine = new DBExaminee();
                newDbExamine.setStuNo(mDbExamLayout.getStuNo());
                newDbExamine.setStuName(mDbExamLayout.getStuName());
                mDbExaminee = newDbExamine;
                //对比
                isComparison = true;
                inquiryDialog.dismiss();
                faceFragment.goContinueDetectFace();
            }
        });


        inquiryDialog.setSearchListener(new InquiryDialog.Search() {
            @Override
            public void search(String msg, int type) {
                mViewModel.getStudent(type, msg, mExanCode, mSeCode);
            }
        });

        mPopExamPlan = new PopExamPlan(this, new PopExamPlan.PopListener() {
            @Override
            public void close(DBExamPlan dbExamPlan) {

                semExanCode(dbExamPlan.getExamCode());
                mDataBinding.tvExamname.setText(dbExamPlan.getExamName());

            }
        });

        mPopExamPlan.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_todown);
                isComparison = false;

            }
        });

        mDataBinding.ivChooseExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_toup);

                mPopExamPlan.showAtLocation(mDataBinding.bgMain, Gravity.BOTTOM, 0, 0);
            }
        });
        handler.postDelayed(runnable, TIME);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        faceFragment.goContinueDetectFace();
        handler.postDelayed(runnable, TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        faceFragment.closeFace();
        handler.removeCallbacks(runnable);
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

        //判断是否可以刷脸
        mViewModel.getmCanSign().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.e("TagSnake", integer + ":数据是否拥有");
                if (integer > 0) {
                    //有数据刷脸 普通刷脸无效
                    faceResultDialog.setType(false);
                } else {
                    faceResultDialog.setType(true);

                }
            }
        });

        //刷脸记录
        mViewModel.getmDBExamExport().observe(this, new Observer<DBExamExport>() {
            @Override
            public void onChanged(DBExamExport dbExamExport) {
//                for (DBExamExport item : saveList) {
//                    if (item.getStuNo().equals(dbExamExport.getStuNo())) {
//
//                    }
//                }
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
                    saveList.set(haveIndex, dbExamExport);
                } else {
                    saveList.add(0, dbExamExport);
                }
                mDataBinding.tvVerifyNumber.setText(saveList.size() + "/" + mStudentNumber);
                mAdapter.notifyDataSetChanged();
            }
        });

        //场次数据
        mViewModel.getmDBExamArrange().observe(this, new Observer<DBExamArrange>() {
            @Override
            public void onChanged(DBExamArrange dbExamArrange) {
                if (dbExamArrange == null) {
                    Toast.makeText(getApplicationContext(), "当前考试计划没有正在进行中的场次", Toast.LENGTH_SHORT).show();
                    return;
                }
                setmSeCode(dbExamArrange.getSeCode());
                mDataBinding.tvSession.setText(dbExamArrange.getSeName());
                //考场总数
                mDataBinding.tvSessionAll.setText(DBOperation.getRoomList(dbExamArrange.getSeCode()).size() + "");
                mStudentNumber = DBOperation.getStudentNumber(mExanCode, mSeCode);
//                mDataBinding.tvVerifyNumber.setText(saveList.size() + "/" + );
                mDataBinding.tvVerifyNumber.setText("0/" + mStudentNumber);
                saveList.clear();

            }
        });

        //用户基础数据，
        mViewModel.getmCheckVersionData().observe(this, new Observer<DBExaminee>() {
            @Override
            public void onChanged(DBExaminee dbExaminee) {
                if (dbExaminee == null)
                    if (!isComparison) {
                        faceResultDialog.setType(false);
                    } else
                        faceFragment.goContinueDetectFace();
                else {
//                    Log.e("TagSnake", mDetectResult.faceNum + "::" + mExanCode + "::" + mSeCode);

                    mDbExaminee = dbExaminee;
                    mViewModel.getSeatAbout(mDetectResult.faceNum, mExanCode, mSeCode);
                }
            }
        });

        //用户考场数据
        mViewModel.getmSeat().observe(this, new Observer<DBExamLayout>() {
            @Override
            public void onChanged(DBExamLayout dbExamLayout) {
                if (dbExamLayout != null) {
                    mDbExamLayout = dbExamLayout;
                    if (isComparison) {
                        //比对状态
                        faceComparedDialog.show();
                        //人员的考场和座位
                        faceComparedDialog.setMsg(mDbExaminee);
                        faceComparedDialog.setSate(dbExamLayout);
                        faceComparedDialog.setNumber(Float.toString(mDetectResult.similarity));
//                        faceComparedDialog.setLeftPhoto(base64);
                    } else {
                        //识别状态
                        if (mExamCodeList.size() == 0)
                            mViewModel.canSign(dbExamLayout.getId());
//                            faceResultDialog.setType(true);
                        else if (mExamCodeList.indexOf(dbExamLayout.getSiteCode()) > 0) {
                            mViewModel.canSign(dbExamLayout.getId());
//                            faceResultDialog.setType(true);
                        } else
                            faceResultDialog.setType(false);
                    }
                } else {
                    if (!isComparison) {
                        faceResultDialog.setType(false);
                    } else {
                        faceFragment.goContinueDetectFace();
                    }
                }
            }
        });

        mViewModel.getmStudent().observe(this, new Observer<DBExamLayout>() {
            @Override
            public void onChanged(DBExamLayout dbExamLayout) {
                if (dbExamLayout != null) {
                    inquiryDialog.setDbExamLayout(dbExamLayout);
                } else {
                    Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //选择考场
    public void chooseExamination(View view) {
        Bundle _b = new Bundle();
        _b.putString("SE_CODE", mSeCode);
        startActivityForResultByRouter(ARouterPath.CHOOSE_VENVE, 121101, _b);
    }

    public void setting(View view) {
        startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
    }

    public void audit(View view) {
        faceFragment.closeFace();
        inquiryDialog.show();
    }

    //页面返回接收
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121101 && resultCode == 1) {
            mExamCodeList.clear();
            mExamCodeList.addAll(data.getStringArrayListExtra("list"));
            mDataBinding.tvSessionAll.setText(mExamCodeList.size() + "");

        }
    }

    /*这个是返回人脸数据和图片
     * */
    public void getFace(FaceDetectResult detectResult) {
//        this.base64 = base64;
        if (isComparison) {
//            Log.e("TagSnake", mDbExaminee.getStuNo() + ":" + detectResult.faceNum);

            if (mDbExaminee.getStuNo().equals(detectResult.faceNum)) {//detectResult.faceNum)) {
                //对比数据成功
                this.mDetectResult = detectResult;
//                mDetectResult.faceNum = "210221112007641";

                mViewModel.quickPeople(detectResult.faceNum, mExanCode);
            } else {
//                isComparison = false;
                faceFragment.goContinueDetectFace();
            }
        } else {
//            Log.e("TagSnake", detectResult.similarity + ":" + detectResult.faceNum);
            if (detectResult.similarity > 0.7f) {
                this.mDetectResult = detectResult;
//                mDetectResult.faceNum = "210221112007641";
                mViewModel.quickPeople(mDetectResult.faceNum, mExanCode);
            } else {
                faceResultDialog.setType(false);
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
