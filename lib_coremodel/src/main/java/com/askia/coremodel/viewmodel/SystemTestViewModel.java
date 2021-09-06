package com.askia.coremodel.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

public class SystemTestViewModel extends BaseViewModel{
    //camera
    public MutableLiveData<String> cameraLiveData = new MutableLiveData<>();
    //screen
    public MutableLiveData<String> screenLiveData = new MutableLiveData<>();
    //voice
    public MutableLiveData<String> voiceLiveData = new MutableLiveData<>();
    //face
    public MutableLiveData<String> faceLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getCameraLiveData() {
        return cameraLiveData;
    }

    public MutableLiveData<String> getScreenLiveData() {
        return screenLiveData;
    }

    public MutableLiveData<String> getFaceLiveData() {
        return faceLiveData;
    }

    public MutableLiveData<String> getVoiceLiveData() {
        return voiceLiveData;
    }

    public void testCamera(Context context){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivity(intent);
    }
}
