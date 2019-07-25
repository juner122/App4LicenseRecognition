package com.kernal.smartvision.ocr;

/**
 *  这里封装了模板参数，根据不同的类型返回了不同的模板，具体参数在具体的参数类中；OcrVin: vin 类， OcrPhoneNumber: 手机号类。
 */
public  class OcrTypeHelper {
   public float leftPointXPercent;
    public float leftPointYPercent;
    public float widthPercent;
    public float heightPercent;
    public float namePositionXPercent;
    public float namePositionYPercent;
    public int nameTextSize;
    public String ocrTypeName;
    public String ocrId;
    public String importTemplateID;

    OcrTypeHelper ocrTypeHelper = null;

    public  OcrTypeHelper(int currentType,int screenDirection) {
        if (currentType == 1){
            // vin
            ocrTypeHelper = new OcrVin(screenDirection);
        }else if (currentType == 2){
            // 手机号码
            ocrTypeHelper = new OcrPhoneNumber(screenDirection);
        }
        }
    public OcrTypeHelper() {
    }

    public OcrTypeHelper getOcr(){
        return ocrTypeHelper;
    }
    @Override
    public String toString() {
        String result = "";
        result = "OcrTypeHeler: leftpointX :" + leftPointXPercent + ",leftPointYPercent:" + leftPointYPercent + ",widthPercent:" + widthPercent + ",heightPercent:" + heightPercent
        + ",namePointX:" + namePositionXPercent + ",namePointY:" + namePositionYPercent;
        return  result;
    }
}
