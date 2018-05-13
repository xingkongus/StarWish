package us.xingkong.visionlibrary.operator;

import vision.core.Operator;

/**
 * 模糊
 *
 * Created by 饶翰新 on 2018/4/13.
 */
public class Cameo extends Convolve3x3 {


    public Cameo(){
        super();
        kerlen = Operator.Cameo;
    }


    @Override
    public String OperatorName() {
        return "浮雕";
    }
}
