package com.lncucc.authentication.activitys;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.ImageUtil;
import com.askia.coremodel.datamodel.data.StudentBean;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.rtc.Constants;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActRoomListBinding;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by ymy
 * Description：
 * Date:2023/2/17 14:35
 */
@Route(path = ARouterPath.ROOM_LIST)
public class RoomListActivity extends BaseActivity {
    private ActRoomListBinding mBinding;
    String roomNo,seCode,examCode;
    private List<StudentBean> mDataList;

    @Override
    public void onInit() {
        roomNo = getIntent().getStringExtra("roomNo");
        seCode = getIntent().getStringExtra("seCode");
        examCode = getIntent().getStringExtra("examCode");
        if (null != roomNo){
            mBinding.tvRoom.setText("考场"+roomNo);
        }
        mBinding.llBack.setOnClickListener(v -> finish());
        requestStudentLayout();
        RxBus2.getInstance().register(this);
    }

    /**
     * 刷新座位表
     */
    private void requestStudentLayout(){
        mDataList = DBOperation.queryStudentByRoomAndSeCode(examCode,seCode,roomNo);
        //填充占位数据
        if (mDataList.size() > 7 && mDataList.size() < 30) {
            mDataList.add(7, new StudentBean());
        } else if (mDataList.size() == 30) {
            mDataList.add(7, new StudentBean());
            mDataList.add(31, new StudentBean());
        }
        mBinding.gridview.setDataList(mDataList);
        mBinding.gridview.requestLayout();
        mBinding.llStudentDetail.setVisibility(View.GONE);
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_room_list);
    }

    //接收座位列表点击事件
    @Subscribe(code = 1)
    public void onGetItemClickEvent(String position){
        showStuDetail(mDataList.get(Integer.parseInt(position)).getId());
    }

    //查询考生验证信息
    private void showStuDetail(String id){
        if (TextUtils.isEmpty(id)){
            Toast.makeText(this,"当前考生暂无验证信息！", Toast.LENGTH_SHORT).show();
            mBinding.llStudentDetail.setVisibility(View.GONE);
            return;
        }
        mBinding.llStudentDetail.setVisibility(View.VISIBLE);
        DBExamExport export = DBOperation.getExamExportById(id);

        String path = UN_ZIP_PATH + File.separator + export.getExamCode() + "/photo/" + export.getStuNo() + ".jpg";
        Log.e("TagSnake path",path);
        //转换file
        File file = new File(path);
        if (file.exists()) {
            if (mBinding.ivPhotoOne.getVisibility()!= View.VISIBLE)
                mBinding.ivPhotoOne.setVisibility(View.VISIBLE);
            Bitmap bt = ImageUtil.getRotateNewBitmap(path);
            mBinding.ivPhotoOne.setImageBitmap(bt);
        }else {
            mBinding.ivPhotoOne.setVisibility(View.INVISIBLE);
        }
        String pathT = Constants.STU_EXPORT + File.separator + export.getSeCode() + File.separator + "photo" + File.separator + export.getStuNo() + ".jpg";
        File file1 = new File(pathT);
        if (file1.exists()) {
            try {
                FileInputStream fiss = new FileInputStream(file1);
                Bitmap bt  = BitmapFactory.decodeStream(fiss);
                mBinding.ivPhotoTwo.setImageBitmap(bt);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        mBinding.tvStudentName.setText(export.getStuName());
        if (DBOperation.quickPeople(export.getStuNo(), export.getExamCode()).size() > 0 ){
            DBExaminee dbExaminee = DBOperation.quickPeople(export.getStuNo(), export.getExamCode()).get(0);
            mBinding.tvStudentSex.setText(dbExaminee.getGender());
            mBinding.tvStudentEthnic.setText(dbExaminee.getNation());
            setResult(export.getVerifyResult());
            String liveAddress = DBOperation.getLiveAddress(dbExaminee.getStuNo());
            mBinding.tvAddress.setText(liveAddress);
            mBinding.tvFaceValue.setText(export.getMatchRate());
            DBExamLayout layout = DBOperation.getStudentInfo(export.getExamCode(), export.getStuNo(),export.getSeCode());
            mBinding.tvTicketNumber.setText(layout.getExReNum());
            mBinding.tvIdCard.setText(layout.getIdCard());
            mBinding.tvExamSubjects.setText(layout.getSeName());
            mBinding.tvRoom.setText(layout.getRoomNo());
            mBinding.tvSeatNumber.setText(layout.getSeatNo());
            mBinding.tvExaminationRoom.setText(layout.getRoomNo());
        }

        switch (export.getHealthCode()){

        }
    }

    public void setResult(String type) {
        if ("1".equals(type)) {
            mBinding.ivStudentType.setImageResource(R.mipmap.icon_type_success);
            mBinding.tvTypeSuccess.setVisibility(View.VISIBLE);
            mBinding.tvTypeFaile.setVisibility(View.GONE);
        } else if ("2".equals(type)) {
            mBinding.ivStudentType.setImageResource(R.mipmap.icon_type_faile);
            mBinding.tvTypeSuccess.setVisibility(View.GONE);
            mBinding.tvTypeFaile.setVisibility(View.VISIBLE);
            mBinding.tvTypeFaile.setText("失败");
        } else {
            mBinding.ivStudentType.setImageResource(R.mipmap.icon_cunyi);
            mBinding.tvTypeSuccess.setVisibility(View.GONE);
            mBinding.tvTypeFaile.setVisibility(View.VISIBLE);
            mBinding.tvTypeFaile.setText("存疑");
        }
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
