package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

/**
 * Create bt she:
 *
 * @date 2021/8/13
 */
public class SelectpalnbysitecodeData extends BaseResponseData {
    private List<ResultBean> result;

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {

        private String examCode;
        private String examName;

        public String getExamCode() {
            return examCode;
        }

        public void setExamCode(String examCode) {
            this.examCode = examCode;
        }

        public String getExamName() {
            return examName;
        }

        public void setExamName(String examName) {
            this.examName = examName;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "examCode='" + examCode + '\'' +
                    ", examName='" + examName + '\'' +
                    '}';
        }
    }
}
