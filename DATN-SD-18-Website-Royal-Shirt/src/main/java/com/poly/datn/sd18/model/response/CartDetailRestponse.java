package com.poly.datn.sd18.model.response;

public interface CartDetailRestponse {
     Integer getId();
     Integer getProductDetailId();
     String getProductName();
     String getColorName();
     String getSizeName();
     Integer getQuantity();
     Float getPrice();
     Integer getDiscount();
}
