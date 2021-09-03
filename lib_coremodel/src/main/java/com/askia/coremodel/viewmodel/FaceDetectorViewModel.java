package com.askia.coremodel.viewmodel;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.face.CompareResult;
import com.askia.coremodel.datamodel.face.FaceServer;
import com.askia.coremodel.util.ImageUtil;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 考试计划名+ 科目名
 */

public class FaceDetectorViewModel extends BaseViewModel {
    private MutableLiveData<Integer> mSavePhoto = new MutableLiveData<>();

    public MutableLiveData<Integer> getmSavePhoto() {
        return mSavePhoto;
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
