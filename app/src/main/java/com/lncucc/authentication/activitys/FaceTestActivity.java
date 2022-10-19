package com.lncucc.authentication.activitys;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.coremodel.rtc.FileUtil;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.unicom.facedetect.detect.FaceDetectManager;
import com.unicom.facedetect.detect.FaceDetectResult;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ymy
 * Description：测试人脸比对
 * Date:2022/10/17 11:12
 */
@Route(path = ARouterPath.FACE_TEST)
public class FaceTestActivity extends AppCompatActivity {

    //比对结果
    private ArrayList<FaceDetectResult> resultList = new ArrayList<>();
    //人脸库导入结果
    private ArrayList<String> faceList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView();
        initEvent();
    }

    private void initEvent() {
        //导入人脸库
    }

    /**
     * 导入目标文件内的人脸照片到人脸库
     */
    private void importFaceModel() {
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "FaceModel";

        File f = new File(path);
        if (!f.exists()) {
            boolean b = f.mkdir();
            if (!b) ToastUtils.showShort("FaceModel文件夹创建失败,请手动创建并导入人脸库照片");
            return;
        }

        //获取模型目录图片列表
        List<File> fileList = FileUtils.listFilesInDir(path);
        if (fileList.isEmpty()) {
            ToastUtils.showShort("FaceMode is empty!");
        }

        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            if (!faceList.isEmpty()) faceList.clear();

            for (File file : fileList) {
                //获取人脸库图片名
                String imageName = file.getName();
                //获取bytes
                byte[] bytes = FileUtil.getBytesByFile(file.getPath());
                //插入人脸库你并获取faceId
                String faceId = FaceDetectManager.getInstance().addFace(imageName, imageName, bytes);
                if (!"".equals(faceId)) {
                    Log.e(imageName, "插入成功");
                } else {
                    Log.e(imageName, "插入失败");
                }
                emitter.onNext(imageName + "-" + faceId);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        d.dispose();
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        //下游接收消息
                        Log.e("onResponse->", s);
                        faceList.add(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("face import->", Objects.requireNonNull(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        Log.e("face import", "onComplete!");
                        ToastUtils.showShort("导入完成");
                    }
                });
    }

    /**
     * 人脸比对
     *
     * @return 返回比对结果集
     */
    private List<FaceDetectResult> compare() {
        //报名照片
        String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "FaceTarget";

        File f = new File(path);
        if (!f.exists()) {
            boolean b = f.mkdir();
            if (!b) ToastUtils.showShort("FaceTarget文件夹创建失败,请手动创建并导入需要比对的照片");
            return null;
        }
        List<File> list = FileUtils.listFilesInDir(path);
        if (list.isEmpty()) {
            ToastUtils.showShort("FaceTarget is empty!");
        }

        //准备开始比对，清空结果集
        if (!resultList.isEmpty()) resultList.clear();

        for (File file : list) {
            //获取文件名
            String fileName = file.getName();
            //获取bytes
            byte[] bytes = FileUtil.getBytesByFile(file.getPath());
            //获取特征值
            float[] feature = FaceDetectManager.getInstance().getFaceFeatureByData(bytes);
            //比对,阈值设置为0.85
            FaceDetectResult result = FaceDetectManager.getInstance().faceDetect(feature, 0.85f);
            //添加到结果集
            resultList.add(result);
        }
        return resultList;
    }
}
