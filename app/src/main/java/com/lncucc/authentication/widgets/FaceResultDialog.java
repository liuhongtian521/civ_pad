package com.lncucc.authentication.widgets;

import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.lncucc.authentication.R;

/**
 * Create bt she:
 * 刷脸结果
 *
 * @date 2021/8/5
 */
public class FaceResultDialog extends BaseDialog {

    View mView;
    ImageView ivClose;

    private DialogClickBackListener onListener;

    private CountDownTimer mCountDownTimer;


    public FaceResultDialog(Context context, DialogClickBackListener dialogClickBackListener) {
        super(context, R.style.DialogTheme);
        mView = getLayoutInflater().inflate(R.layout.dialog_face_finish, null);
        setContentView(mView);

        ivClose=mView.findViewById(R.id.iv_close);

        this.onListener = dialogClickBackListener;

        mCountDownTimer = new CountDownTimer(2 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                onListener.dissMiss();
            }
        };


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.dissMiss();
            }
        });

    }


    public interface onActivateListener {
        void onActivate();
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
