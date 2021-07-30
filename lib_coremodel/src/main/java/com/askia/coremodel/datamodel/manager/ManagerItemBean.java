package com.askia.coremodel.datamodel.manager;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ManagerItemBean implements Serializable {


    @SerializedName("item")
    private List<ItemBean> item;

    public List<ItemBean> getItem() {
        return item;
    }

    public void setItem(List<ItemBean> item) {
        this.item = item;
    }

    public static class ItemBean {
        @SerializedName("name")
        private String name;
        @SerializedName("pic")
        private String pic;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
