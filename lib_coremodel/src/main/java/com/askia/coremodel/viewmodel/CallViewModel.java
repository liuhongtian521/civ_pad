package com.askia.coremodel.viewmodel;


import androidx.lifecycle.MutableLiveData;

import com.arcsoft.face.FaceFeature;
import com.askia.coremodel.datamodel.face.CompareResult;
import com.askia.coremodel.datamodel.face.FaceServer;
import com.askia.coremodel.datamodel.http.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.HttpAddressBookBean;
import com.askia.coremodel.datamodel.http.repository.NetDataRepository;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CallViewModel extends BaseViewModel {
    private MutableLiveData<HttpAddressBookBean> mAddressBookLiveData = new MutableLiveData<>();

    public MutableLiveData<HttpAddressBookBean> getAddressBookLiveData() {
        return mAddressBookLiveData;
    }

   public void getAddressBook()
   {

       List<HttpAddressBookBean.resultItem> list = new ArrayList<>();
       HttpAddressBookBean.resultItem item1 = new HttpAddressBookBean.resultItem();
       item1.setHeadImg("");
       item1.setName("李飞");
       item1.setRelation("父亲");
       item1.setPhone("13812344321");
       list.add(item1);

       HttpAddressBookBean.resultItem item2 = new HttpAddressBookBean.resultItem();
       item2.setHeadImg("");
       item2.setName("李焕英");
       item2.setRelation("母亲");
       item2.setPhone("13812344321");
       list.add(item2);

       HttpAddressBookBean addressBookBean = new HttpAddressBookBean();
       addressBookBean.setResult(list);
       addressBookBean.setSuccess(true);
       mAddressBookLiveData.postValue(addressBookBean);

      /* NetDataRepository.queryConsumeDetail(id,queryTime+"-01",type,cardcode).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .safeSubscribe(new Observer<ConsumeRecordData>() {
                   @Override
                   public void onSubscribe(Disposable d) {
                       mDisposable.add(d);
                   }

                   @Override
                   public void onNext(ConsumeRecordData data)
                   {
                       mRecordLiveData.setValue(data);
                   }

                   @Override
                   public void onError(Throwable e) {
                       e.printStackTrace();
                       ConsumeRecordData rulesData = new ConsumeRecordData();
                       if (e instanceof SocketTimeoutException) {
                           rulesData.setStatus(ResponseCode.ConnectTimeOut);
                           rulesData.setMsg("连接超时，请重试");
                       } else {
                           rulesData.setStatus(ResponseCode.ServerNotResponding);
                           rulesData.setMsg("服务器无响应，请重试");
                       }
                       mRecordLiveData.setValue(rulesData);
                   }
                   @Override
                   public void onComplete() {

                   }
               });*/
   }

}
