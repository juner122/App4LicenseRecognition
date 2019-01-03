package com.eb.new_line_seller.bean;


//商品列表ITEM
public class ProductListItemEntity {


    String ProductName;
    String ProductPic;
    String ProductTS;
    String Price;

    public ProductListItemEntity(String productName, String productPic, String productTS, String price) {
        ProductName = productName;
        ProductPic = productPic;
        ProductTS = productTS;
        Price = price;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPic() {
        return ProductPic;
    }

    public void setProductPic(String productPic) {
        ProductPic = productPic;
    }

    public String getProductTS() {
        return ProductTS;
    }

    public void setProductTS(String productTS) {
        ProductTS = productTS;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
