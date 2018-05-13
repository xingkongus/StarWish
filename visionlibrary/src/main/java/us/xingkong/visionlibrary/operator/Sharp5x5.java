package us.xingkong.visionlibrary.operator;

import vision.core.Operator;

/**
 * 模糊
 * <p>
 * Created by 饶翰新 on 2018/4/13.
 */
public class Sharp5x5 extends Convolve5x5 {


    public Sharp5x5() {
        super();

        float sum = 0;
        kerlen = new float[Operator.Sharp.length][];
        for (int i = 0; i < kerlen.length; i++) {
            kerlen[i] = new float[kerlen.length];
            for (int j = 0; j < kerlen.length; j++) {
                sum += (Operator.Sharp[i][j]);
            }
        }
        if (sum != 0)
            for (int i = 0; i < kerlen.length; i++) {
                for (int j = 0; j < kerlen.length; j++) {
                    kerlen[i][j] = Operator.Sharp[i][j] / sum;
                }
            }
    }


    @Override
    public String OperatorName() {
        return "锐化";
    }
}
