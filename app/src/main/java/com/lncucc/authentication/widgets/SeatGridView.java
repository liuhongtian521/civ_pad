package com.lncucc.authentication.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.askia.coremodel.datamodel.data.StudentBean;
import com.lncucc.authentication.R;
import com.ttsea.jrxbus2.RxBus2;
import java.util.List;

/**
 * Created by ymy
 * Description：自定义座位列表，8行4列排列，数据按纵向填充
 * Date:2023/2/21 14:44
 */
public class SeatGridView extends ViewGroup {
    private List<StudentBean> mDataList;  // 数据列表

    public SeatGridView(Context context) {
        super(context);
        init();
    }

    public SeatGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 初始化数据列表
        int width = getResources().getDimensionPixelSize(R.dimen.dp_125);
        int height = getResources().getDimensionPixelSize(R.dimen.dp_35);
        int margin = getResources().getDimensionPixelSize(R.dimen.dp_10);
        //设置
        LinearLayout.LayoutParams paramsLayoutExpire = new LinearLayout.LayoutParams(width, height);
        paramsLayoutExpire.setMargins(margin, margin, margin, margin);
        paramsLayoutExpire.setLayoutDirection(LinearLayout.HORIZONTAL);

        // 添加32个TextView，考场最多只有30人，多2个为占位使用。
        for (int i = 0; i < 32; i++) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(paramsLayoutExpire);
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setTextSize(12);
            if (mDataList != null) {
                textView.setText(mDataList.get(i).getName());
            }
            //2个占位
            textView.setBackgroundColor(Color.parseColor("#ffffff"));
            textView.setPadding(0, 20, 0, 0);
            addView(textView, i);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 计算每个TextView的宽度和高度
        int childWidth = getResources().getDimensionPixelSize(R.dimen.dp_115);
        int childHeight = getResources().getDimensionPixelOffset(R.dimen.dp_35);
        int childCount = getChildCount();
        int col = 0;
        //遍历child，并按竖向排列，4列 8行，第8和第31为占位使用。
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int finalI = i;
            //绑定item点击事件，空白占位位置不绑定
            if (i != 7 && i != 31){
                int finalI1 = i;
                child.setOnClickListener(v -> {
                    RxBus2.getInstance().post(1, finalI1 + "");
                });
            }
            //计算行数
            int row = i % 8;
            //设置child坐标 x,y 分别为left,top
            int x = col * childWidth;
            int y = row * childHeight;
            // 绘制完第8行后开始换行,col++
            if (row % 8 == 0) {
                y = 0;
                if (i != 0) {
                    ++col;
                    x = col * childWidth;
                }
            }
            //二次判定定位x坐标
            if (col == 0) {
                x = 0;
            }
            child.layout(x + 14, y + 14, x + childWidth, y + childHeight);
        }
    }

    public void setDataList(List<StudentBean> list) {
        this.mDataList = list;
        assert list != null;
        //重新绑定赋值
        for (int i = 0; i < this.mDataList.size(); i ++){
            if (i != 7 && i != 31){
                //item 赋值
                StudentBean bean = this.mDataList.get(i);
                ((TextView) getChildAt(i)).setText(bean.getSeatNo() +"    "+bean.getName());
                //设置不同背景色，标识每个学生的验证状态
                //未验证
                if ("0".equals(this.mDataList.get(i).getValidationState())){
                    getChildAt(i).setBackgroundColor(Color.parseColor("#3A90FF"));
                }else if ("1".equals(this.mDataList.get(i).getValidationState())){
                    getChildAt(i).setBackgroundColor(Color.parseColor("#0EBD35"));
                }else if ("2".equals(this.mDataList.get(i).getValidationState())){
                    getChildAt(i).setBackgroundColor(Color.parseColor("#FF5F5F"));
                }else if ("3".equals(this.mDataList.get(i).getValidationState())){
                    getChildAt(i).setBackgroundColor(Color.parseColor("#FBB630"));
                }else {
                    getChildAt(i).setBackgroundColor(Color.parseColor("#808080"));
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
