package com.lncucc.authentication.activitys;

import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;

import java.io.File;

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
     * 导入目标文件内的人脸照片到人脸库
     */
    private void importFaceModel(){
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "FaceModel";

    }

    private Float compare() {
        return 0f;
    }
}
