package com.askia.coremodel.datamodel.http.download;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

class RxUtil {
    private Subject<Object> mBus;
    private static RxUtil sInstance;

    private RxUtil() {
        mBus = PublishSubject.create().toSerialized();
    }

    static RxUtil getInstance() {
        if (sInstance == null) {
            synchronized (RxUtil.class) {
                if (sInstance == null) {
                    //单例模式之双重检测：线程一在此之前线程二到达了位置[1],如果此处不二次判断，那么线程二执行到这里的时候还会重新new
                    sInstance = new RxUtil();
                }
            }
        }
        return sInstance;
    }

    private Subject<Object> getBus() {
        return mBus;
    }

    static void send(Object obj) {
        if (getInstance().getBus().hasObservers()) {
            getInstance().getBus().onNext(obj);
        }
    }

    static Observable<Object> toObservable() {
        return getInstance().getBus();
    }

    static Observable<BGADownloadProgressEvent> getDownloadEventObservable()
    {
        return getInstance().toObservable().ofType(BGADownloadProgressEvent.class).observeOn(AndroidSchedulers.mainThread());
    }
}
