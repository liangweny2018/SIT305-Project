package com.project.sit305.utils;


import com.project.sit305.bean.ParkingDataBean;

import java.util.Comparator;

public class PriceSort implements Comparator<ParkingDataBean> {

    @Override         //两个参数是泛型的对象
    public int compare(ParkingDataBean o1, ParkingDataBean o2) {
        //按照姓名的升序排列，前面加个负号就按照降序排列
        return (int) (o1.getPrice() - o2.getPrice());
    }
}
