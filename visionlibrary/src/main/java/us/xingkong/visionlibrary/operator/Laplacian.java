package us.xingkong.visionlibrary.operator;

import vision.core.Operator;

/**
 * 模糊
 *
 * Created by 饶翰新 on 2018/4/13.
 */
public class Laplacian extends Convolve3x3 {


    public Laplacian(){
        super();
        kerlen = Operator.Laplacian;
    }


    @Override
    public String OperatorName() {
        return "拉普拉斯边缘检测";
    }
}
