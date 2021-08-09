package com.lncucc.authentication.activitys;

import androidx.databinding.BaseObservable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.recyclerview.FRecyclerViewAdapter;
import com.askia.common.recyclerview.FViewHolderHelper;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActAuthenticationBinding;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.FaceResultDialog;
import com.lncucc.authentication.widgets.InquiryDialog;
import com.lncucc.authentication.widgets.PeopleMsgDialog;

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

    private FRecyclerViewAdapter<Map<String, Object>> mAdapter;

    @Override
    public void onInit() {
        mAdapter = new FRecyclerViewAdapter<Map<String, Object>>(mDataBinding.rvList, R.layout.item_verify) {
            @Override
            protected void fillData(FViewHolderHelper viewHolderHelper, int position, Map<String, Object> model) {
                viewHolderHelper.setText(R.id.tv_item_verify_name,model.get("name").toString());

            }
        };

        peopleMsgDialog = new PeopleMsgDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                peopleMsgDialog.dismiss();
            }

            @Override
            public void backType(int type) {

            }
        });

        faceResultDialog = new FaceResultDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                faceResultDialog.dismiss();
            }

            @Override
            public void backType(int type) {
                faceResultDialog.dismiss();

                if (type == 0) {
                    //不通过
                } else if (type == 1) {
                    //存疑

                } else {
                    //通过

                }

            }
        });

        inquiryDialog = new InquiryDialog(this, new DialogClickBackListener() {
            @Override
            public void dissMiss() {
                inquiryDialog.dismiss();
            }

            @Override
            public void backType(int type) {
                inquiryDialog.dismiss();
                //对比


            }
        });

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_authentication);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
