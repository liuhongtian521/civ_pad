package com.askia.coremodel.datamodel.http.entities;

import java.io.Serializable;

/**
 * Create bt she:
 * 压缩包下载
 *
 * @date 2021/8/12
 */
public class GetZipData extends BaseResponseData {
    private ResultBean result;
    public ResultBean getResult() {
        return result;
    }
    public void setResult(ResultBean result) {
        this.result = result;
    }
    public static class ResultBean implements Serializable {
        private String minioUrl;//下载地址
        private String bucketName;//下载中间连接
        private String filename;//下载后缀
        private String fileUrl;
        private String minioName;//用户名
        private String minioPass;//密码
        private String result;//
        private String msg;//错误信息

        private String examCode;

        public String getExamCode() {
            return examCode;
        }

        public void setExamCode(String examCode) {
            this.examCode = examCode;
        }

        public String getMinioUrl() {
            return minioUrl;
        }

        public void setMinioUrl(String minioUrl) {
            this.minioUrl = minioUrl;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getMinioName() {
            return minioName;
        }

        public void setMinioName(String minioName) {
            this.minioName = minioName;
        }

        public String getMinioPass() {
            return minioPass;
        }

        public void setMinioPass(String minioPass) {
            this.minioPass = minioPass;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "minioUrl='" + minioUrl + '\'' +
                    ", bucketName='" + bucketName + '\'' +
                    ", filename='" + filename + '\'' +
                    ", fileUrl='" + fileUrl + '\'' +
                    ", minioName='" + minioName + '\'' +
                    ", minioPass='" + minioPass + '\'' +
                    ", result='" + result + '\'' +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

}
