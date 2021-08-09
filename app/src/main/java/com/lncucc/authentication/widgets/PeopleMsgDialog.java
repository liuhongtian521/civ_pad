package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lncucc.authentication.R;

/**
 * Create bt she:
 * 考生信息
 *
 * @date 2021/8/9
 */
public class PeopleMsgDialog extends BaseDialog {
    View mView;
    private DialogClickBackListener onListener;

    ImageView ivBack, ivPhotoLeft, ivPhotoRight, ivSuccess, ivFaile;

    TextView tvName, tvSex, tvNationality, tvSubjectsName, tvExaminationRoom, tvTicketNumber, tvSeatNumber, tvIdCard;
    TextView tvAddress, tvFaceValue;
    TextView tvTypeSuccess, tvTypeFaile;


    public PeopleMsgDialog(Context context, DialogClickBackListener dialogClickBackListener) {
        super(context, R.style.DialogTheme);
        mView = getLayoutInflater().inflate(R.layout.dialog_people_msg, null);
        setContentView(mView);

        this.onListener = dialogClickBackListener;

        ivBack = mView.findViewById(R.id.iv_back);//返回
        ivPhotoLeft = mView.findViewById(R.id.iv_photo_one);//左面人脸
        ivPhotoRight = mView.findViewById(R.id.iv_photo_two);//右边人脸
        ivSuccess = mView.findViewById(R.id.iv_student_success);//结果
        ivFaile = mView.findViewById(R.id.iv_student_faile);

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

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.dissMiss();
            }
        });

    }

    public void setResult(boolean type) {
        if (type) {
            ivSuccess.setVisibility(View.VISIBLE);
            ivFaile.setVisibility(View.GONE);
            tvTypeSuccess.setVisibility(View.VISIBLE);
            tvFaceValue.setVisibility(View.GONE);
        } else {
            ivSuccess.setVisibility(View.GONE);
            ivFaile.setVisibility(View.VISIBLE);
            tvTypeSuccess.setVisibility(View.GONE);
            tvFaceValue.setVisibility(View.VISIBLE);
        }
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


}
