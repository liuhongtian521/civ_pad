package com.lncucc.authentication.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.lncucc.authentication.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;

/**
 * Create bt she:
 *
 * @date 2021/8/6
 */
public class InquiryDialog extends BaseDialog {
    View mView;
    private DialogClickBackListener onListener;

    private Search onSearch;

    RelativeLayout relLeft, relRight, relChoose;
    TextView tvChooseText;

    EditText editExamNum, editCard;

    ImageView ivBack, ivDel, ivFace;

    TextView tvName, tvExamNum, tvIdCard, tvAddress, tvAddressNum;

    Button btnSearch, btnClose, btnNext;

    LinearLayout linePeople;

    private DBExamLayout dbExamLayout;


    public InquiryDialog(Context context, DialogClickBackListener dialogClickBackListener) {
        super(context, R.style.DialogTheme);

        mView = getLayoutInflater().inflate(R.layout.dialog_inquiry, null);
        setContentView(mView);
        relLeft = mView.findViewById(R.id.rel_title_one);
        relRight = mView.findViewById(R.id.rel_title_two);
        relChoose = mView.findViewById(R.id.rel_click);

        tvChooseText = mView.findViewById(R.id.tv_click_title);
        ivBack = mView.findViewById(R.id.iv_back);
        ivDel = mView.findViewById(R.id.iv_del);
        ivFace = mView.findViewById(R.id.iv_face);

        editExamNum = mView.findViewById(R.id.edit_exam_number);
        editCard = mView.findViewById(R.id.edit_idcard);
        tvName = mView.findViewById(R.id.tv_people_name);
        tvExamNum = mView.findViewById(R.id.tv_cardno);
        tvIdCard = mView.findViewById(R.id.tv_idCard);
        tvAddress = mView.findViewById(R.id.tv_address);
        tvAddressNum = mView.findViewById(R.id.tv_address_no);

        btnSearch = mView.findViewById(R.id.btn_search);
        btnClose = mView.findViewById(R.id.btn_quick);
        btnNext = mView.findViewById(R.id.btn_next);

        linePeople = mView.findViewById(R.id.line_people);

        this.onListener = dialogClickBackListener;
        ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editExamNum.setText("");
                editCard.setText("");
            }
        });
        relLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relLeft.setVisibility(View.GONE);
                relRight.setVisibility(View.VISIBLE);
                relChoose.setVisibility(View.VISIBLE);
                tvChooseText.setText("准考证查询");
                editExamNum.setVisibility(View.VISIBLE);
                editExamNum.setText("");
                editCard.setVisibility(View.GONE);

                linePeople.setVisibility(View.GONE);
            }
        });
        relRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relLeft.setVisibility(View.VISIBLE);
                relRight.setVisibility(View.GONE);
                relChoose.setVisibility(View.VISIBLE);
                tvChooseText.setText("身份证号查询");

                editExamNum.setVisibility(View.GONE);
                editCard.setVisibility(View.VISIBLE);
                editCard.setText("");

                linePeople.setVisibility(View.GONE);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.dissMiss();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (relRight.getVisibility() == View.VISIBLE) {
                    //准考证查询
                    if (!"".equals(editExamNum.getText().toString().trim()))
                        onSearch.search(editExamNum.getText().toString().trim(), 0);
                } else if (relLeft.getVisibility() == View.VISIBLE) {
                    //身份证号查询
                    if (!"".equals(editCard.getText().toString().trim())) {
                        onSearch.search(editCard.getText().toString().trim(), 1);
                    }
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.dissMiss();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListener.backType(1);
            }
        });

        setCanceledOnTouchOutside(false);


        editExamNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("TagSnake","editExamNum.setOnEditorActionListener");
                if (!"".equals(editExamNum.getText().toString().trim()))
                    onSearch.search(editExamNum.getText().toString().trim(), 0);
                return true;
            }
        });

        editCard.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (!"".equals(editCard.getText().toString().trim())) {
                    onSearch.search(editCard.getText().toString().trim(), 1);
                }
                return true;
            }
        });

    }

    public void setSearchListener(Search onSearch) {
        this.onSearch = onSearch;
    }

    public void setDbExamLayout(DBExamLayout dbExamLayout) {
        this.dbExamLayout = dbExamLayout;
        linePeople.setVisibility(View.VISIBLE);

        tvName.setText(dbExamLayout.getStuName());
        tvExamNum.setText(dbExamLayout.getExReNum());
        tvIdCard.setText(dbExamLayout.getIdCard());
        tvAddress.setText(dbExamLayout.getSiteName());
        tvAddressNum.setText(dbExamLayout.getSeatNo());
//        String path = UN_ZIP_PATH + File.separator + "110206/photo/" + dbExamLayout.getStuNo() + ".jpg";
        String examCode = dbExamLayout.getExamCode();
        String path = UN_ZIP_PATH + File.separator + examCode + File.separator + "photo" + File.separator + dbExamLayout.getStuNo() + ".jpg";
        //转换file
        File file = new File(path);
        if (file.exists()) {
            try {
                FileInputStream fiss = new FileInputStream(file);
                Bitmap bt  = BitmapFactory.decodeStream(fiss);
//                    Bitmap bts =BitmapFactory.decodeStream(getClass().getResourceAsStream(path));
//                viewHolderHelper.setImageBitmap(R.id.iv_item_head_two, bt);
                ivFace.setImageBitmap(bt);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ivFace.setVisibility(View.INVISIBLE);
            }

            //转换bitmap
//            Bitmap bt = BitmapFactory.decodeFile(path);
//
        }else {
            ivFace.setVisibility(View.INVISIBLE);

        }
    }

    public DBExamLayout getDbExamLayout() {
        return dbExamLayout;
    }

    //搜索用
    public void search() {
        relLeft.setVisibility(View.GONE);
        relRight.setVisibility(View.VISIBLE);
        relChoose.setVisibility(View.VISIBLE);
        tvChooseText.setText("准考证查询");
        editExamNum.setVisibility(View.VISIBLE);
        editExamNum.setText("");
//        editExamNum.setText("030105200130");

        editCard.setVisibility(View.GONE);

        linePeople.setVisibility(View.GONE);
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

    public interface Search {
        void search(String msg, int type);
    }
}
