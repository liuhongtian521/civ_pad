package com.askia.coremodel.datamodel.http;

import com.askia.coremodel.datamodel.http.entities.BaseResponseData;

public class HttpUnionPayBean extends BaseResponseData
{
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
