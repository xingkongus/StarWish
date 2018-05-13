package us.xingkong.visionlibrary.operator;

import vision.core.Operator;

/**
 * 模糊
 *
 * Created by 饶翰新 on 2018/4/13.
 */
public class Edge5x5 extends Convolve5x5 {


    public Edge5x5(){
        super();
        kerlen = Operator.Edge_5X5;
    }


    @Override
    public String OperatorName() {
        return "边缘提取5x5";
    }
}
