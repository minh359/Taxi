package com.example.taxi;


import java.io.Serializable;

public class Bill   implements Comparable<Bill>,Serializable {

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public String getCarNum() {
        return CarNum;
    }
    public void setCarNum(String carNum) {
        CarNum = carNum;
    }
    public float getDistance() {
        return Distance;
    }
    public void setDistance(float distance) {
        Distance = distance;
    }
    public int getPrice() {
        return Price;
    }
    public void setPrice(int price) {
        Price = price;
    }
    public int getDiscount() {
        return Discount;
    }
    public void setDiscount(int discount) {
        Discount = discount;
    }
    public Bill(){}
    public Bill(String carNum, float distance, int price, int discount) {
        CarNum = carNum;
        Distance = distance;
        Price = price;
        Discount = discount;
    }
    public Bill(int id, String carNum, float distance, int price, int discount) {
        Id = id;
        CarNum = carNum;
        Distance = distance;
        Price = price;
        Discount = discount;
    }
    private  int Id;
    private  String CarNum;
    private  float Distance;
    private  int Price;
    private  int Discount;
    @Override
    public int compareTo(Bill o) {
        return this.getCarNum().compareTo(o.getCarNum());
    }
    public  float getTotal()
    {
        return getDistance()*getPrice()*(100-getDiscount())/100;
    }
}
