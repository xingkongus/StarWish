package us.xingkong.visionlibrary.operator;

import vision.core.Operator;

/**
 * 模糊
 *
 * Created by 饶翰新 on 2018/4/13.
 */
public class Edge3x3 extends Convolve3x3 {


    public Edge3x3(){
        super();
        kerlen = Operator.Edge_3X3;
    }


    @Override
    public String OperatorName() {
        return "边缘提取3x3";
    }
}
