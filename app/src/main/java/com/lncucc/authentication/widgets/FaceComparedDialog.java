package com.lncucc.authentication.widgets;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lncucc.authentication.R;

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

        ivClose = mView.findViewById(R.id.iv_back);
        tvName = mView.findViewById(R.id.tv_face_name);
        tvCardNo = mView.findViewById(R.id.tv_cardno);
        tvAddress = mView.findViewById(R.id.tv_address);
        tvIdcard = mView.findViewById(R.id.tv_idCard);
        tvAddNo = mView.findViewById(R.id.tv_address_no);
        tvNumber = mView.findViewById(R.id.tv_fraction);

        ivPhotoOne = mView.findViewById(R.id.iv_photo_one);
        ivPhotoTwo = mView.findViewById(R.id.iv_photo_two);

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
                onListener.backType(2);
            }
        });
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

    public void setNumber(String number) {
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
}
