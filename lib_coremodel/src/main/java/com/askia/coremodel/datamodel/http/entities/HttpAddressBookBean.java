package com.askia.coremodel.datamodel.http.entities;

import java.util.ArrayList;
import java.util.List;

public class HttpAddressBookBean extends BaseResponseData{
    private List<resultItem> result = new ArrayList<>();

    public List<resultItem> getResult() {
        return result;
    }

    public void setResult(List<resultItem> result) {
        this.result = result;
    }

    public static class resultItem{
        private String headImg;
        private String name;
        private String relation;
        private String phone;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
