package com.lncucc.authentication.activitys;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.bigdata.facedetect.FaceDetect;

/**
 * Created by ymy
 * Description：测试人脸比对
 * Date:2022/10/17 11:12
 */
@Route(path = ARouterPath.FACE_TEST)
public class FaceTestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 人脸比对测试
     *
     * @param source 报名照片
     * @param target 入场照片
     * @return 比对结果
     */
    private Float compare(String source, String target) {


        FaceDetect detect = new FaceDetect();


        return 0f;
    }
}
