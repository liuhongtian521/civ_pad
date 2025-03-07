package com.lncucc.authentication.widgets;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.askia.coremodel.datamodel.database.operation.DBOperation;
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

    LinearLayout linSuccess, linFaile;
    ImageView healthCode;
    ImageView faileImage;
    TextView tvStatus;

    TextView faileStatus;
    private MediaPlayer player;
    private Context mContext;


    public FaceResultDialog(Context context, DialogClickBackListener dialogClickBackListener) {
        super(context, R.style.DialogTheme);
        mView = getLayoutInflater().inflate(R.layout.dialog_face_finish, null);
        setContentView(mView);
        this.mContext = context;
        setCanceledOnTouchOutside(false);
        ivClose = mView.findViewById(R.id.iv_close);
        linSuccess = mView.findViewById(R.id.line_face_success);
        linFaile = mView.findViewById(R.id.line_face_faile);
        faileImage = mView.findViewById(R.id.icon_face_faile_img);
        healthCode = mView.findViewById(R.id.iv_health_code);
        tvStatus = mView.findViewById(R.id.tv_health_status);
        faileStatus = mView.findViewById(R.id.icon_face_faile_message);
        this.onListener = dialogClickBackListener;

        //获取高级设置中的验证间隔时间
        int interval = DBOperation.getIntervalVerifyTime();

        mCountDownTimer = new CountDownTimer(interval * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (onListener != null)
                    if (linSuccess.getVisibility() == View.VISIBLE) {
                        onListener.backType(2);
                    } else {
                        onListener.dissMiss();
                    }
            }
        };


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownTimer.cancel();
                if (linSuccess.getVisibility() == View.VISIBLE) {
                    onListener.backType(2);
                } else {
                    onListener.dissMiss();
                }
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
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
    }

    public void setType(boolean type, String code,String message) {
//        1，验证通过+绿码  语音提示：验证通过
//
//        2，验证通过+黄码/红码/未知  语音提示：健康码异常
//
//        3，验证失败+绿码，语音提示：验证失败
//
//        4，验证失败+黄码/红码/未知，语音提示：健康码异常
        //code 0绿 1黄  2红 3未知

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        super.show();
        fullScreenImmersive(getWindow().getDecorView());
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        mCountDownTimer.start();
       //暂时去除健康码语音提示，保留通过和请重试。
        if (type) {
            player = MediaPlayer.create(mContext, R.raw.tongguo);
            switch (code){
                case "0":
                    linSuccess.setVisibility(View.VISIBLE);
                    healthCode.setImageResource(R.mipmap.icon_code_green);
                    tvStatus.setText("健康码正常");
                    tvStatus.setTextColor(Color.parseColor("#0EBD35"));
                    linFaile.setVisibility(View.GONE);
                    break;
                case "1":
//                    player = MediaPlayer.create(mContext, R.raw.health_code_abnormal);
                    linSuccess.setVisibility(View.VISIBLE);
                    healthCode.setImageResource(R.mipmap.icon_code_yellow);
                    tvStatus.setText("健康码未知");
                    tvStatus.setTextColor(Color.parseColor("#FFC047"));
                    linFaile.setVisibility(View.GONE);
                    break;
                case "2":
//                    player = MediaPlayer.create(mContext, R.raw.health_code_abnormal);
                    linSuccess.setVisibility(View.VISIBLE);
                    healthCode.setImageResource(R.mipmap.icon_code_red);
                    tvStatus.setText("健康码未知");
                    tvStatus.setTextColor(Color.parseColor("#FF615F"));
                    linFaile.setVisibility(View.GONE);
                    break;
                case "3":
//                    player = MediaPlayer.create(mContext, R.raw.health_code_abnormal);
                    linSuccess.setVisibility(View.VISIBLE);
                    healthCode.setImageResource(R.mipmap.icon_code_none);
                    tvStatus.setTextColor(Color.parseColor("#6F7783"));
                    tvStatus.setText("健康码未知");
                    linFaile.setVisibility(View.GONE);
                    break;
            }
        }
        if (!type){
            if(code.equals("4")){
                player = MediaPlayer.create(mContext, R.raw.qingchongshi);
                linSuccess.setVisibility(View.GONE);
                faileImage.setImageResource(R.mipmap.icon_face_faile);
                faileStatus.setTextColor(Color.parseColor("#6F7783"));
                faileStatus.setText("                 验证失败"+"\n"+message);
                linFaile.setVisibility(View.VISIBLE);
            }else{
                player = MediaPlayer.create(mContext, R.raw.qingchongshi);
                faileStatus.setText("  验证失败");
                linSuccess.setVisibility(View.GONE);
                linFaile.setVisibility(View.VISIBLE);
            }

        }
        if (player != null) {
            player.start();
        }
    }

}
