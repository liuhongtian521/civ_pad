package com.lncucc.authentication.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.askia.common.util.ImageUtil;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.blankj.utilcode.util.FileUtils;
import com.lncucc.authentication.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;


/**
 * 考生详情dialog
 */
public class StudentInfoDialog extends BaseDialog {

    private DBExamLayout stuInfo;
    private View mView;
    private ImageView mImageView;
    private TextView mName;
    private TextView mSex;
    private TextView mNation;
    private TextView mCensus;
    private TextView mSubjectName;//场次名称
    private TextView mExamination;//考场
    private TextView mTicketNum; //准考证号
    private TextView mSeatNum; //座位号
    private TextView mIdNo;//身份证号

    public StudentInfoDialog(Context context, DBExamLayout layout) {
        super(context, R.style.DialogTheme);
        this.stuInfo = layout;
        mView = getLayoutInflater().inflate(R.layout.dialog_student_info, null);
        initView();
        setContentView(mView);
    }

    private void initView() {
        mView.findViewById(R.id.iv_back).setOnClickListener(v -> dismiss());
        mImageView = mView.findViewById(R.id.iv_photo);
        mName = mView.findViewById(R.id.tv_student_name);
        mSex = mView.findViewById(R.id.tv_student_sex);
        mNation = mView.findViewById(R.id.tv_student_ethnic);
        mCensus = mView.findViewById(R.id.tv_census_value);
        mSubjectName = mView.findViewById(R.id.tv_exam_subjects);
        mExamination = mView.findViewById(R.id.tv_examination_room);
        mTicketNum = mView.findViewById(R.id.tv_ticket_number);
        mSeatNum = mView.findViewById(R.id.tv_seat_number);
        mIdNo = mView.findViewById(R.id.tv_idCard);
    }

    public void showDialog(DBExamLayout layout) {
        this.show();
        if (layout != null) {
            //获取stuNo
            String stuNo = layout.getStuNo();
            //现在没有数据暂时写死
//            String stuNo = "210221112022500";
            //根据stuNo查询居住地址
            String liveAddress = DBOperation.getLiveAddress(stuNo);
            //获取解压包文件夹名称
            //获取照片路径 110206 需替换成考试代码 examCode
            String examCode = layout.getExamCode();
            String filePath = "";
            //获取所有解压的文件夹
            List<File> list = FileUtils.listFilesInDir(UN_ZIP_PATH);
            //遍历获取包含当前examCode的 文件夹名称
            for (File file : list) {
                if (file.getName().contains(examCode)) {
                    filePath = file.getName();
                }
            }
            String path = UN_ZIP_PATH + File.separator + examCode + "/photo/" + stuNo + ".jpg";
            //转换file
            File file = new File(path);
            if (file.exists()) {
                if (mImageView.getVisibility() != View.VISIBLE)
                    mImageView.setVisibility(View.VISIBLE);
                //转换bitmap
//                Bitmap bt = BitmapFactory.decodeFile(path);
                Bitmap bt = ImageUtil.getRotateNewBitmap(path);

                mImageView.setImageBitmap(bt);
            } else {
                mImageView.setVisibility(View.INVISIBLE);
            }
            mName.setText(layout.getStuName());
            mSex.setText(layout.getGender());
            mNation.setText(layout.getNation());
            mCensus.setText(liveAddress);
            mSubjectName.setText(layout.getSubName());
            mExamination.setText(layout.getRoomNo());
            mTicketNum.setText(layout.getExReNum());
            mSeatNum.setText(layout.getSeatNo());
            mIdNo.setText(layout.getIdCard());
        }
    }


}
