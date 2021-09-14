package com.lncucc.authentication.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.askia.common.util.ImageUtil;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.rtc.Constants;
import com.lncucc.authentication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;

/**
 * Create bt she:
 * 考生信息
 *
 * @date 2021/8/9
 */
public class PeopleMsgDialog extends BaseDialog {
    View mView;
    private DialogClickBackListener onListener;

    ImageView ivBack, ivPhotoLeft, ivPhotoRight, ivType;

    TextView tvName, tvSex, tvNationality, tvSubjectsName, tvExaminationRoom, tvTicketNumber, tvSeatNumber, tvIdCard;
    TextView tvAddress, tvFaceValue;
    TextView tvTypeSuccess, tvTypeFaile;
    RelativeLayout rlBack;


    public PeopleMsgDialog(Context context, DialogClickBackListener dialogClickBackListener) {
        super(context, R.style.DialogTheme);
        mView = getLayoutInflater().inflate(R.layout.dialog_people_msg, null);
        setContentView(mView);

        setCanceledOnTouchOutside(false);

        this.onListener = dialogClickBackListener;

        ivBack = mView.findViewById(R.id.iv_back);//返回
        ivPhotoLeft = mView.findViewById(R.id.iv_photo_one);//左面人脸
        ivPhotoRight = mView.findViewById(R.id.iv_photo_two);//右边人脸
        ivType = mView.findViewById(R.id.iv_student_type);//结果

        tvName = mView.findViewById(R.id.tv_student_name);//名字
        tvSex = mView.findViewById(R.id.tv_student_sex);//姓名
        tvNationality = mView.findViewById(R.id.tv_student_ethnic);//民族
        tvSubjectsName = mView.findViewById(R.id.tv_exam_subjects);//考试科目
        tvExaminationRoom = mView.findViewById(R.id.tv_examination_room);//考场
        tvTicketNumber = mView.findViewById(R.id.tv_ticket_number);//准考证号
        tvSeatNumber = mView.findViewById(R.id.tv_seat_number);//座位号
        tvIdCard = mView.findViewById(R.id.tv_idCard);//身份证号
        tvAddress = mView.findViewById(R.id.tv_address);//地址
        tvFaceValue = mView.findViewById(R.id.tv_face_value);//人脸对比度

        tvTypeSuccess = mView.findViewById(R.id.tv_type_success);//结果-成功
        tvTypeFaile = mView.findViewById(R.id.tv_type_faile);//结果-失败

        rlBack = mView.findViewById(R.id.rl_back);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.dissMiss();
            }
        });

    }

    public void setResult(String type) {
        if ("1".equals(type)) {
            ivType.setImageResource(R.drawable.icon_type_success);
            tvTypeSuccess.setVisibility(View.VISIBLE);
            tvTypeFaile.setVisibility(View.GONE);
        } else if ("2".equals(type)) {
            ivType.setImageResource(R.drawable.icon_type_faile);
            tvTypeSuccess.setVisibility(View.GONE);
            tvTypeFaile.setVisibility(View.VISIBLE);
            tvTypeFaile.setText("失败");
        } else {
            ivType.setImageResource(R.drawable.icon_cunyi);
            tvTypeSuccess.setVisibility(View.GONE);
            tvTypeFaile.setVisibility(View.VISIBLE);
            tvTypeFaile.setText("存疑");
        }
    }

    public void setMsg(DBExamExport model) {
        String path = UN_ZIP_PATH + File.separator + model.getExamCode() + "/photo/" + model.getStuNo() + ".jpg";
        Log.e("TagSnake path",path);
        //转换file
        File file = new File(path);
        if (file.exists()) {
            if (ivPhotoLeft.getVisibility()!=View.VISIBLE)
                ivPhotoLeft.setVisibility(View.VISIBLE);
            //转换bitmap
//            Bitmap bt = BitmapFactory.decodeFile(path);
            Bitmap bt = ImageUtil.getRotateNewBitmap(path);
//            viewHolderHelper.setImageBitmap(R.id.iv_item_head_one, bt);
             ivPhotoLeft.setImageBitmap(bt);
        }else {
            ivPhotoLeft.setVisibility(View.INVISIBLE);
        }
        String pathT = Constants.STU_EXPORT + File.separator + model.getSeCode() + File.separator + "photo" + File.separator + model.getStuNo() + ".jpg";
        Log.e("TagSnake pathT",pathT);
        File file1 = new File(pathT);
        if (file1.exists()) {
            //转换bitmap
//            Bitmap bt = BitmapFactory.decodeFile(pathT);
            try {
                FileInputStream fiss = new FileInputStream(file1);
                Bitmap bt  = BitmapFactory.decodeStream(fiss);
//                    Bitmap bts =BitmapFactory.decodeStream(getClass().getResourceAsStream(path));
//                viewHolderHelper.setImageBitmap(R.id.iv_item_head_two, bt);
                ivPhotoRight.setImageBitmap(bt);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            viewHolderHelper.setImageBitmap(R.id.iv_item_head_two, bt);
        }


        tvName.setText(model.getStuName());
        Log.e("TagSnake", model.getStuNo() + "::" + model.getExamCode());
        Log.e("TagSnake", DBOperation.quickPeople(model.getStuNo(), model.getExamCode()).size() + ":");
        DBExaminee dbExaminee = DBOperation.quickPeople(model.getStuNo(), model.getExamCode()).get(0);
        tvSex.setText(dbExaminee.getGender());
        tvNationality.setText(dbExaminee.getNation());
        setResult(model.getVerifyResult());

        String liveAddress = DBOperation.getLiveAddress(dbExaminee.getStuNo());
        tvAddress.setText(liveAddress);
        tvFaceValue.setText(model.getMatchRate());
        DBExamLayout layout = DBOperation.getStudentInfo(model.getExamCode(), model.getStuNo(),model.getSeCode());
        tvTicketNumber.setText(layout.getExReNum());
        tvIdCard.setText(layout.getIdCard());
        setSubjectsName(layout.getSeName());
        setExaminationRoom(layout.getRoomNo());
        setSeatNumber(layout.getSiteName());

    }

    public void setName(String name) {
        tvName.setText(name);
    }

    public void setSex(String sex) {
        tvSex.setText(sex);
    }

    public void setNationality(String nationality) {
        tvNationality.setText(nationality);
    }

    public void setSubjectsName(String subjectsName) {
        tvSubjectsName.setText(subjectsName);
    }

    public void setExaminationRoom(String examinationRoom) {
        tvExaminationRoom.setText(examinationRoom);
    }

    public void setTicketNumber(String ticketNumber) {
        tvTicketNumber.setText(ticketNumber);
    }

    public void setSeatNumber(String seatNumber) {
        tvSeatNumber.setText(seatNumber);
    }

    public void setIdCard(String idCard) {
        tvIdCard.setText(idCard);
    }

    public void setAddress(String address) {
        tvAddress.setText(address);
    }

    public void setFaceValue(String faceValue) {
        tvFaceValue.setText(faceValue);
    }

    @Override
    public void show() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        fullScreenImmersive(getWindow().getDecorView());
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        Window window = this.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
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
