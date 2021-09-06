package com.askia.coremodel.datamodel.system;

import java.io.Serializable;
import java.util.List;

public class SystemTestBean implements Serializable {

    private List<DataBean> data;
    private String name;
    private String description;
    private String minimumCoreVersion;
    private String title;
    private String version;
    private String author;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinimumCoreVersion() {
        return minimumCoreVersion;
    }

    public void setMinimumCoreVersion(String minimumCoreVersion) {
        this.minimumCoreVersion = minimumCoreVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static class DataBean {
        private String name;
        private String pic;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        private int state = 0; // 0待测试 1已测试 2异常

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
