package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lncucc.authentication.R;

import org.w3c.dom.Text;

/**
 * Create bt she:
 *
 * @date 2021/8/6
 */
public class InquiryDialog extends BaseDialog {
    View mView;
    private DialogClickBackListener onListener;

    RelativeLayout relLeft, relRight, relChoose;
    TextView tvChooseText;

    EditText editExamNum, editCard;

    ImageView ivBack, ivDel;

    TextView tvName, tvExamNum, tvIdCard, tvAddress, tvAddressNum;

    Button btnSearch, btnClose, btnNext;

    LinearLayout linePeople;


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

        linePeople=mView.findViewById(R.id.line_people);

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
                search();

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
    }
    //搜索用
    private void search() {


    }
}
