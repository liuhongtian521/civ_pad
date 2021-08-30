package com.lncucc.authentication.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.util.OtherUtils;
import com.lncucc.authentication.R;

import java.io.File;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;

/**
 * Create bt she:
 * 对比结果
 *
 * @date 2021/8/6
 */
public class FaceComparedDialog extends BaseDialog {
    View mView;
    private DialogClickBackListener onListener;

    ImageView ivClose, ivPhotoOne, ivPhotoTwo;
    TextView tvName, tvCardNo, tvAddress, tvIdcard, tvAddNo, tvNumber;
    Button btnNot, btnMaybe, btnSure;


    public FaceComparedDialog(Context context, DialogClickBackListener dialogClickBackListener) {
        super(context, R.style.DialogTheme);

        mView = getLayoutInflater().inflate(R.layout.dialog_face_compared, null);
        setContentView(mView);
        setCanceledOnTouchOutside(false);


        ivClose = mView.findViewById(R.id.iv_back);
        tvName = mView.findViewById(R.id.tv_face_name);
        tvCardNo = mView.findViewById(R.id.tv_cardno);
        tvAddress = mView.findViewById(R.id.tv_address);
        tvIdcard = mView.findViewById(R.id.tv_idCard);
        tvAddNo = mView.findViewById(R.id.tv_address_no);
        tvNumber = mView.findViewById(R.id.tv_fraction);

        ivPhotoOne = mView.findViewById(R.id.iv_photo_left);
        ivPhotoTwo = mView.findViewById(R.id.iv_photo_right);

        btnNot = mView.findViewById(R.id.btn_not);
        btnMaybe = mView.findViewById(R.id.btn_maybe);
        btnSure = mView.findViewById(R.id.btn_sure);

        this.onListener = dialogClickBackListener;

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.dissMiss();
            }
        });
        btnNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.backType(0);
            }
        });
        btnMaybe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.backType(1);
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TagSnake", "btnsure");
                onListener.backType(2);
            }
        });
    }

    public void setSate(DBExamLayout dbExamLayout) {
        setAddress(dbExamLayout.getRoomNo());
        setAddNo(dbExamLayout.getSeatNo());
        setName(dbExamLayout.getStuName());
        setIdcard(dbExamLayout.getIdCard());

        setCardNo(dbExamLayout.getExReNum());

        String pathT = Constants.STU_EXPORT + File.separator + dbExamLayout.getSeCode() + File.separator + "photo" + File.separator + dbExamLayout.getStuNo() + ".jpg";
        String path = UN_ZIP_PATH + File.separator + dbExamLayout.getExamCode() + File.separator + "photo" + File.separator + dbExamLayout.getStuNo() + ".jpg";
//        Log.e("TagSnake",pathT);
        //转换file
        File file = new File(pathT);
        if (file.exists()) {
            //转换bitmap
            Bitmap bt = BitmapFactory.decodeFile(pathT);
            ivPhotoOne.setImageBitmap(bt);
        }
//
//        Log.e("TagSnake",path);

        File file1 = new File(path);
        if (file1.exists()) {
            //转换bitmap
            Bitmap bt = BitmapFactory.decodeFile(path);
            ivPhotoTwo.setImageBitmap(bt);
        }
    }


    public void setName(String name) {
        tvName.setText(name);
    }

    public void setCardNo(String cardNo) {
        tvCardNo.setText(cardNo);
    }

    public void setAddress(String address) {
        tvAddress.setText(address);
    }

    public void setIdcard(String idcard) {
        tvIdcard.setText(idcard);
    }

    public void setAddNo(String addNo) {
        tvAddNo.setText(addNo);
    }

    public void setLeftPhoto(String base64) {
        ivPhotoOne.setImageBitmap(OtherUtils.base64ToImageView(base64));
    }

    public void setNumber(String number) {

        if (number.length() > 4) {
            tvNumber.setText(number.substring(0, 4));
        } else
            tvNumber.setText(number);

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

    public void setMsg(DBExaminee mDbExaminee) {


    }
}
