package com.askia.coremodel.viewmodel;


import androidx.lifecycle.MutableLiveData;

import com.arcsoft.face.FaceFeature;
import com.askia.coremodel.datamodel.face.CompareResult;
import com.askia.coremodel.datamodel.face.FaceServer;


import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FaceAuthViewModel extends BaseViewModel {
    private MutableLiveData<CompareResult> mSearchResultLiveData = new MutableLiveData<>();


    public MutableLiveData<CompareResult> getSearchResultLiveData() {
        return mSearchResultLiveData;
    }



    public void searchFace(FaceFeature frFace) {
        Observable.create((ObservableOnSubscribe<CompareResult>) emitter -> {
            CompareResult compareResult = FaceServer.getInstance().getTopOfFaceLib(frFace);
//                        Log.i(TAG, "subscribe: fr search end = " + System.currentTimeMillis() + " trackId = " + requestId);
            if (compareResult == null) {
                emitter.onError(null);
            } else {
                emitter.onNext(compareResult);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompareResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(CompareResult result) {
                         mSearchResultLiveData.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CompareResult result = new CompareResult(new ArrayList<>());
                        mSearchResultLiveData.postValue(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
