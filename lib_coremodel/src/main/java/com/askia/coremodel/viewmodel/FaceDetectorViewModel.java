package com.askia.coremodel.viewmodel;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.face.CompareResult;
import com.askia.coremodel.datamodel.face.FaceMsgBase;
import com.askia.coremodel.datamodel.face.FaceServer;
import com.askia.coremodel.datamodel.http.download.StorageUtil;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.util.ImageUtil;
import com.bigdata.facedetect.FaceDetect;
import com.blankj.utilcode.util.FileUtils;
import com.unicom.facedetect.detect.FaceDetectManager;
import com.unicom.facedetect.detect.FaceDetectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 人脸处理
 */

public class FaceDetectorViewModel extends BaseViewModel {
    private MutableLiveData<Integer> mSavePhoto = new MutableLiveData<>();
    private MutableLiveData<FaceDetectResult> mFaceDetect = new MutableLiveData<>();

    private MutableLiveData<FaceMsgBase> mFace = new MutableLiveData<>();

    public MutableLiveData<FaceMsgBase> getmFace() {
        return mFace;
    }

    public MutableLiveData<Integer> getmSavePhoto() {
        return mSavePhoto;
    }

    public MutableLiveData<FaceDetectResult> getmFaceDetect() {
        return mFaceDetect;
    }

    public void faceHandle(byte[] nv21, Camera.Size previewSize, int cameraDisplayOrientation) {
        Observable.create((ObservableOnSubscribe<FaceMsgBase>) emitter -> {
            FaceMsgBase faceMsgBase = new FaceMsgBase();
            FaceDetect.FaceColorResult faceResult = FaceDetectManager.getInstance().checkFaceFromNV21(nv21, previewSize.width, previewSize.height, cameraDisplayOrientation);
            faceMsgBase.setFaceColorResult(faceResult);
            faceMsgBase.setNv21(nv21);
            emitter.onNext(faceMsgBase);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FaceMsgBase>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(FaceMsgBase result) {
                        mFace.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFace.postValue(null);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void dataPross(Rect faceRect, byte[] nv21, Camera.Size previewSize, boolean isComputen, String seCode, String stuNo) {

        Observable.create((ObservableOnSubscribe<FaceDetectResult>) emitter -> {

            YuvImage image = new YuvImage(nv21, ImageFormat.NV21, previewSize.width, previewSize.height, null);
            ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
            byte[] jpegData = null;
            image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 80, outputSteam);
            jpegData = outputSteam.toByteArray();

            float[] feature = FaceDetectManager.getInstance().getFaceFeatureByData(jpegData);
            FaceDetectResult detectResult = FaceDetectManager.getInstance().faceDetect(feature, 0.85f);
            if (detectResult == null) {
                Log.e("TagSnakesnake", "detect result ->    null");
                emitter.onNext(null);
//                mFaceDetect.postValue(null);
                return;
            }
            if (detectResult != null) {
                Bitmap bitmap = null;
                BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 2;
                Log.e("TagSnakesnake", "刷脸分数:" + detectResult.similarity);
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, options);
                if (faceRect != null) {
                    bitmap = ImageUtil.imageCrop(bitmap, faceRect);
                }
                String path = "";
                if (isComputen) {
                    path = Constants.STU_EXPORT + File.separator + seCode + File.separator + "photo" + File.separator + stuNo + ".jpg";
                } else if (seCode != null && detectResult.faceNum != null && !"".equals(detectResult.faceNum)) {
                    path = Constants.STU_EXPORT + File.separator + seCode + File.separator + "photo" + File.separator + detectResult.faceNum + ".jpg";
                }


                if (FileUtils.isFileExists(path)) {
                    File file = FileUtils.getFileByPath(path);
                    if (file != null)
                        file.delete();
//                    StorageUtil.deleteFile(file);
                }
                bitmap = com.blankj.utilcode.util.ImageUtils.rotate(bitmap, 0, 0, 0);
                bitmap = ImageUtil.sampleSize(bitmap);
                boolean back = com.blankj.utilcode.util.ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG);

                bitmap.recycle();
                File file1 = new File(path);
                if (file1.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(file1);
                        Bitmap bt = BitmapFactory.decodeStream(fis);
//                    Bitmap bts =BitmapFactory.decodeStream(getClass().getResourceAsStream(path));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //转换bitmap
//                Bitmap bt = BitmapFactory.decodeFile(path);
                }
                emitter.onNext(detectResult);

            } else {
                emitter.onNext(null);
//                mFaceDetect.postValue(null);
            }

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FaceDetectResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(FaceDetectResult result) {
                        mFaceDetect.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mFaceDetect.postValue(null);
                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }

    public void savePhoto(byte[] jpegData, String path, Rect faceRect) {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            //                        Log.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
            if (!"".equals(path)) {
                Bitmap bitmap = null;
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inSampleSize = 2;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, options);
                    if (faceRect != null) {
                        bitmap = ImageUtil.imageCrop(bitmap, faceRect);
                    }
                    bitmap = com.blankj.utilcode.util.ImageUtils.rotate(bitmap, 0, 0, 0);
                    bitmap = ImageUtil.sampleSize(bitmap);
                    com.blankj.utilcode.util.ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG);
                    bitmap.recycle();
                } catch (Exception e) {
                    Log.e("TagSnake", Log.getStackTraceString(e));
                }
            }

            emitter.onNext(1);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer result) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
