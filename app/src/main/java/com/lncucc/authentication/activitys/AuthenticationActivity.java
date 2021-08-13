package com.lncucc.authentication.activitys;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.recyclerview.FRecyclerViewAdapter;
import com.askia.common.recyclerview.FViewHolderHelper;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.viewmodel.AuthenticationViewModel;
import com.askia.coremodel.viewmodel.CheckUpdateViewModel;
import com.blankj.utilcode.util.FileUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActAuthenticationBinding;
import com.lncucc.authentication.fragments.FaceShowFragment;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.FaceComparedDialog;
import com.lncucc.authentication.widgets.FaceResultDialog;
import com.lncucc.authentication.widgets.InquiryDialog;
import com.lncucc.authentication.widgets.PeopleMsgDialog;
import com.unicom.facedetect.detect.FaceDetectResult;

import java.util.Map;

/**
 * Create bt she:
 * 身份验证
 *
 * @date 2021/8/5
 */
@Route(path = ARouterPath.IDENTIFY_ACTIVITY)
public class AuthenticationActivity extends BaseActivity {
    private ActAuthenticationBinding mDataBinding;

    //考生信息 dialog
    private PeopleMsgDialog peopleMsgDialog;
    //刷脸结果
    private FaceResultDialog faceResultDialog;
    //搜索学生
    private InquiryDialog inquiryDialog;
    //对比结果
    private FaceComparedDialog faceComparedDialog;
    private FRecyclerViewAdapter<Map<String, Object>> mAdapter;
    private FaceShowFragment faceFragment;
    private AuthenticationViewModel mViewModel;

    private String mExanCode = "";

    private FaceDetectResult mDetectResult;
    private DBExaminee mDbExaminee;
    private String base64;

    private boolean isComparison = false;

    @Override
    public void onInit() {
        faceFragment = (FaceShowFragment) getFragment(ARouterPath.FACE_SHOW_ACTIVITY);
        addFragment(faceFragment, R.id.frame_layout);

        mAdapter = new FRecyclerViewAdapter<Map<String, Object>>(mDataBinding.rvList, R.layout.item_verify) {
            @Override
            protected void fillData(FViewHolderHelper viewHolderHelper, int position, Map<String, Object> model) {
                viewHolderHelper.setText(R.id.tv_item_verify_name, model.get("name").toString());
            }
        };

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
                    mViewModel.setMsg(mDbExaminee, base64, System.currentTimeMillis() + "", "0", Float.toString(mDetectResult.similarity));
                } else if (type == 1) {
                    //存疑
                    mViewModel.setMsg(mDbExaminee, base64, System.currentTimeMillis() + "", "2", Float.toString(mDetectResult.similarity));
                } else {
                    //通过
                    mViewModel.setMsg(mDbExaminee, base64, System.currentTimeMillis() + "", "1", Float.toString(mDetectResult.similarity));
                }
            }
        });

        faceComparedDialog = new FaceComparedDialog(this, new DialogClickBackListener() {
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
                    mViewModel.setMsg(mDbExaminee, base64, System.currentTimeMillis() + "", "0", Float.toString(mDetectResult.similarity));
                } else if (type == 1) {
                    //存疑
                    mViewModel.setMsg(mDbExaminee, base64, System.currentTimeMillis() + "", "2", Float.toString(mDetectResult.similarity));
                } else {
                    //通过
                    mViewModel.setMsg(mDbExaminee, base64, System.currentTimeMillis() + "", "1", Float.toString(mDetectResult.similarity));
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
                //对比
                isComparison = true;
                inquiryDialog.dismiss();
                faceFragment.goContinueDetectFace();
            }
        });

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
        mViewModel.getmCheckVersionData().observe(this, new Observer<DBExaminee>() {
            @Override
            public void onChanged(DBExaminee dbExaminee) {
                mDbExaminee = dbExaminee;
                if (isComparison) {
                    mViewModel.getSeatAbout(mDetectResult.faceNum, mExanCode);
                } else {
                    if (dbExaminee != null) {
                        faceResultDialog.setType(false);
                    } else {
                        faceFragment.goContinueDetectFace();
                    }
                }
            }
        });
        mViewModel.getmSeat().observe(this, new Observer<DBExamLayout>() {
            @Override
            public void onChanged(DBExamLayout dbExamLayout) {
                if (dbExamLayout != null) {
                    faceComparedDialog.show();
                    //人员的考场和座位
                    faceComparedDialog.setSate(dbExamLayout);
                    faceComparedDialog.setNumber(Float.toString(mDetectResult.similarity));
                    faceComparedDialog.setLeftPhoto(base64);
                } else {
                    faceFragment.goContinueDetectFace();
                }
            }
        });
    }

    //选择考场
    public void chooseExamination(View view) {
        startActivityByRouter(ARouterPath.CHOOSE_VENVE);
    }

    public void setting(View view) {
        startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
    }

    /*这个是返回人脸数据和图片
    * */
    public void getFace(FaceDetectResult detectResult, String base64) {
        this.base64 = base64;
        if (isComparison) {
            // mDbExaminee
            if (mDbExaminee.getStuNo().equals(detectResult.faceNum)) {
                //对比数据成功
                this.mDetectResult = detectResult;
                mViewModel.quickPeople(detectResult.faceNum, mExanCode);

            } else {
                faceFragment.goContinueDetectFace();
            }
        } else {
            if (detectResult.similarity > 80f) {
                this.mDetectResult = detectResult;
                faceResultDialog.setType(true);
                mViewModel.quickPeople(detectResult.faceNum, mExanCode);
            } else {
                faceResultDialog.setType(false);
            }
        }
    }

}
