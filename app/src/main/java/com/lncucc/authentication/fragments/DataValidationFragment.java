package com.lncucc.authentication.fragments;

import static com.askia.coremodel.rtc.Constants.FULL_SCREEN_FLAG;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ValidationDataAdapter;
import com.lncucc.authentication.databinding.FragmentDataValidationBinding;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.PeopleMsgDialog;
import com.lncucc.authentication.widgets.pop.BottomPopUpWindow;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 验证数据查看
 */
public class DataValidationFragment extends BaseFragment implements DialogClickBackListener {
    private FragmentDataValidationBinding mBinding;
    private List<DBExamExport> mList;
    private ValidationDataAdapter mAdapter;
    private List<DBExamExport> tempList = new ArrayList<>();
    private PeopleMsgDialog peopleMsgDialog;
    private List<DBExamArrange> sessionList;
    private String seCode;

    @Override
    public void onInit() {
        mList = DBOperation.getVerifyList();
        sessionList = DBOperation.getAllExamArrange();
        tempList.clear();
        tempList.addAll(mList);
        mBinding.rlDataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ValidationDataAdapter(tempList);
        mBinding.rlDataView.setAdapter(mAdapter);
        if (null !=sessionList && sessionList.size() >0 && sessionList.get(0).getSeName() != null){
            seCode = sessionList.get(0).getSeCode();
            mBinding.tvSession.setText(sessionList.get(0).getSeName());
        }
        RxBus2.getInstance().register(this);
        //默认查询当前场次验证数据
        query(null);
        initEvent();
    }

    private void initEvent(){
        peopleMsgDialog = new PeopleMsgDialog(getActivity(),this);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DBExamExport examExport = DBOperation.getExamExportById(tempList.get(position).getId());
            peopleMsgDialog.setMsg(examExport);
            peopleMsgDialog.show();
        });

        mBinding.editExamNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                query(null);
            }
            return false;
        });
        mBinding.rlSession.setOnClickListener(v -> showPopUp());
    }

    @Override
    public void onInitViewModel() {

    }

    private void showPopUp() {
        View parent = mBinding.llContainer;
        BottomPopUpWindow pop = new BottomPopUpWindow(getActivity(), sessionList);
        pop.setFocusable(false);
        pop.showAtLocation(parent, Gravity.CENTER, 0, 0);
        pop.getContentView().setSystemUiVisibility(FULL_SCREEN_FLAG);
        pop.setFocusable(true);
        pop.update();
    }

    @Subscribe(code = 0)
    public void onGetSessionEvent(String index) {
        int position = Integer.parseInt(index);
        //获取选中场次
        seCode = sessionList.get(position).getSeCode();
        //更新selected view
        mBinding.tvSession.setText(sessionList.get(position).getSeName());
        query(null);
    }

    public void query(View view) {
        String queryParams = mBinding.editExamNumber.getText().toString().trim();
        mList = DBOperation.getDBExportByIdNo(queryParams,seCode);
        KeyboardUtils.hideSoftInput(Objects.requireNonNull(getActivity()));
        tempList.clear();
        if (mList.size() > 0) {
            tempList.addAll(mList);
        } else {
            MyToastUtils.error("没有查询到该考生信息！", Toast.LENGTH_SHORT);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_validation,container,false);
        mBinding.setHandles(this);
        return mBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    public void dissMiss() {
        peopleMsgDialog.dismiss();
    }

    @Override
    public void backType(int type) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
