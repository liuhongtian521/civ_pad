package com.lncucc.authentication.fragments;

import static com.askia.coremodel.rtc.Constants.FULL_SCREEN_FLAG;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.SEService;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseFragment;
import com.askia.common.widget.GridSpacingItemDecoration;
import com.askia.coremodel.datamodel.data.ExamExportGroupBean;
import com.askia.coremodel.datamodel.data.ValidationDataBean;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.lncucc.authentication.R;
import com.lncucc.authentication.activitys.RoomListActivity;
import com.lncucc.authentication.adapters.ValidationDataStatisticsAdapter;
import com.lncucc.authentication.databinding.FragmentValidataionDataStatisticsFragmentBinding;
import com.lncucc.authentication.widgets.pop.BottomPopUpWindow;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymy
 * Description：验证数据统计
 * Date:2023/2/15 13:29
 */
public class ValidationDataStatisticsFragment extends BaseFragment {

    FragmentValidataionDataStatisticsFragmentBinding mBinding;
    private List<DBExamArrange> sessionList;
    private String seCode;
    private List<ExamExportGroupBean> mValidationList = new ArrayList<>();
    private ValidationDataStatisticsAdapter mAdapter;
    private String examCode;

    @Override
    public void onInit() {
        sessionList = DBOperation.getAllExamArrange();
        if (null !=sessionList && sessionList.size() >0 && sessionList.get(0).getSeName() != null){
            seCode = sessionList.get(0).getSeCode();
            examCode = sessionList.get(0).getExamCode();
            mBinding.tvSession.setText(sessionList.get(0).getSeName());
        }
        mBinding.tvSession.setOnClickListener(v -> showPopUp());
        //获取当前场次验证数据
        mValidationList = DBOperation.queryValidationDataBySeCode(seCode);
        mValidationList = DBOperation.getAllExaminationHall(mValidationList, seCode,examCode);
        RxBus2.getInstance().register(this);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),4);
        mAdapter = new ValidationDataStatisticsAdapter(mValidationList);
        mBinding.recyclerSession.setLayoutManager(manager);
        mBinding.recyclerSession.setAdapter(mAdapter);
        mBinding.recyclerSession.addItemDecoration(new GridSpacingItemDecoration(4,30,30,false));
        //设置顶部数量
        setValidationNum();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //跳转到考场页面
            Bundle b = new Bundle();
            b.putString("roomNo",mValidationList.get(position).getRoomNo());
            b.putString("seCode",seCode);
            b.putString("examCode",examCode);
            ARouter.getInstance().build(ARouterPath.ROOM_LIST).with(b).navigation();
        });
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_validataion_data_statistics_fragment,container,false);
        return mBinding.getRoot();
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

    private void setValidationNum(){
        ValidationDataBean bean = DBOperation.queryStuNumBySeCode(seCode, examCode);
        mBinding.tvNotValidation.setText(String.format("%d", bean.getNotValidation()));
        mBinding.tvValidationPass.setText(bean.getPassValidation() + "");
        mBinding.tvValidationDoubt.setText(bean.getDoubtValidation() + "");
        mBinding.tvValidationNoPass.setText(bean.getNotPassValidation() + "");
    }

    @Subscribe(code = 0)
    public void onGetSessionEvent(String index) {
        int position = Integer.parseInt(index);
        //获取选中场次
        seCode = sessionList.get(position).getSeCode();
        mValidationList.clear();
        List<ExamExportGroupBean> examExportGroupBeans = DBOperation.queryValidationDataBySeCode(seCode);
        mValidationList.addAll(DBOperation.getAllExaminationHall(examExportGroupBeans, seCode,examCode));
        //更新selected view
        mBinding.tvSession.setText(sessionList.get(position).getSeName());
        setValidationNum();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSubscribeViewModel() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
