package com.juner.mvp.bean;


import java.util.List;

public class CarVin3 {



    public class ktype{

        String manuName;//品牌
        String modelName;//型号
        String constructionType;//车级
        String cylinderCapacityCcm;// CCM（气缸排量
        String yearOfConstrFrom ;//上市时间

        public String getManuName() {
            return manuName;
        }

        public void setManuName(String manuName) {
            this.manuName = manuName;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getConstructionType() {
            return constructionType;
        }

        public void setConstructionType(String constructionType) {
            this.constructionType = constructionType;
        }

        public String getCylinderCapacityCcm() {
            return cylinderCapacityCcm;
        }

        public void setCylinderCapacityCcm(String cylinderCapacityCcm) {
            this.cylinderCapacityCcm = cylinderCapacityCcm;
        }

        public String getYearOfConstrFrom() {
            return yearOfConstrFrom;
        }

        public void setYearOfConstrFrom(String yearOfConstrFrom) {
            this.yearOfConstrFrom = yearOfConstrFrom;
        }
    }

}
