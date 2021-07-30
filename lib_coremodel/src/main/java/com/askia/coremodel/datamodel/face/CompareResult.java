package com.askia.coremodel.datamodel.face;


import java.io.Serializable;
import java.util.List;

public class CompareResult implements Serializable {

    private List<Messa> data;

    public CompareResult(List<Messa> data) {
        this.data = data;
    }

    public List<Messa> getData() {
        return data;
    }

    public void setData(List<Messa> data) {
        this.data = data;
    }

    public static class Messa implements Serializable {
        private String userName;
        private float similar;
        private int trackId;

        public Messa(String userName, float similar){
            this.userName=userName;
            this.similar=similar;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public float getSimilar() {
            return similar;
        }

        public void setSimilar(float similar) {
            this.similar = similar;
        }

        public int getTrackId() {
            return trackId;
        }

        public void setTrackId(int trackId) {
            this.trackId = trackId;
        }

    }
}
