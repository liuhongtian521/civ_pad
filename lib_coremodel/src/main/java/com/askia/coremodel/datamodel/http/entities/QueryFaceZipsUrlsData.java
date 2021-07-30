package com.askia.coremodel.datamodel.http.entities;
import java.util.List;

public class QueryFaceZipsUrlsData extends BaseResponseData
{


    /**
     * result : {"flag":0,"imageList":[{"timeStamp":"20201028102223","url":"这是一个测试的格式，flag=0不需要更新"}]}
     * timestamp : 1603851743445
     */

    private ResultBean result;
    private long timestamp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static class ResultBean {
        /**
         * flag : 0
         * imageList : [{"timeStamp":"20201028102223","url":"这是一个测试的格式，flag=0不需要更新"}]
         */

        private int flag;
        private List<ImageListBean> imageList;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public List<ImageListBean> getImageList() {
            return imageList;
        }

        public void setImageList(List<ImageListBean> imageList) {
            this.imageList = imageList;
        }

        public static class ImageListBean {
            /**
             * timeStamp : 20201028102223
             * url : 这是一个测试的格式，flag=0不需要更新
             */

            private String timeStamp;
            private String url;

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
