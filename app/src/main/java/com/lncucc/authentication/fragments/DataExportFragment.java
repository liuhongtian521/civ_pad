package com.lncucc.authentication.fragments;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DataExportViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentExportBinding;
import com.lncucc.authentication.widgets.pop.BottomPopUpWindow;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据导出
 */
public class DataExportFragment extends BaseFragment {
    private List<Boolean> list;
    private List<DBExamArrange> sessionList;
    private String examCode = "";

    private FragmentExportBinding exportBinding;
    private DataExportViewModel exportViewModel;
    private static final int FULL_SCREEN_FLAG = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

    @Override
    public void onInit() {
        exportBinding.rlArrange.setOnClickListener(v -> showPopUp());
        sessionList = DBOperation.getDBExamArrange();
        if (sessionList != null && sessionList.size() > 0){
            exportBinding.tvSession.setText(sessionList.get(0).getSeName());
            examCode = sessionList.get(0).getExamCode();
        }
        RxBus2.getInstance().register(this);
    }

    @Override
    public void onInitViewModel() {
        exportViewModel = ViewModelProviders.of(getActivity()).get(DataExportViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        exportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_export, container, false);
        exportBinding.setHandles(this);
        return exportBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {
        exportViewModel.doExport().observe(this, result -> {
            if ("导出成功".equals(result)){
                LogsUtil.saveOperationLogs("数据导出");
            }
            MyToastUtils.error(result, Toast.LENGTH_SHORT);
        });
    }
    @Subscribe(code = 0)
    public void onGetSessionEvent(String index){
        int position = Integer.parseInt(index);
        exportBinding.tvSession.setText(sessionList.get(position).getSeName());
        examCode = sessionList.get(position).getExamCode();
    }

    private void showPopUp(){
        View parent = exportBinding.llContainer;
        BottomPopUpWindow pop = new BottomPopUpWindow(getActivity(),sessionList);
        pop.setFocusable(false);
        pop.showAtLocation(parent, Gravity.CENTER,0,0);
        pop.getContentView().setSystemUiVisibility(FULL_SCREEN_FLAG);
        pop.setFocusable(true);
        pop.update();
    }

    public void export(View view) {
        list = new ArrayList<>();
        list.add(exportBinding.sbNet.isChecked());
        list.add(exportBinding.sbUsb.isChecked());
        list.add(exportBinding.sbSdcard.isChecked());

        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)) {
                count++;
            }
        }
        if (count > 1) {
            MyToastUtils.error("只能选择一种导入方式!", Toast.LENGTH_SHORT);
            return;
        }
        if (count == 0) {
            MyToastUtils.error("请选择一种导出方式!", Toast.LENGTH_SHORT);
            return;
        }

        if (exportBinding.sbNet.isChecked()) {
            MyToastUtils.error("敬请期待！", Toast.LENGTH_SHORT);
        } else if (exportBinding.sbUsb.isChecked()) {
            MyToastUtils.error("敬请期待！", Toast.LENGTH_SHORT);
        } else {
            exportViewModel.doDataExport(examCode);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
