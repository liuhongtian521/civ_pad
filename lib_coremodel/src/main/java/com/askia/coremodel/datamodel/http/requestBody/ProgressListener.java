package com.askia.coremodel.datamodel.http.requestBody;

public interface ProgressListener {
    void onProgress(long hasWrittenLen, long totalLen);
}