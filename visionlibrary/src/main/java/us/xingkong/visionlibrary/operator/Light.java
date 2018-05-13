package us.xingkong.visionlibrary.operator;

import android.content.Context;

import vision.core.Core;
import vision.image.VisionImage;

/**
 * 明暗处理
 *
 * Created by 饶翰新 on 2018/4/13.
 */

public class Light extends ImageOperator {

    public int d;

    public Light(){
        super();
        d = 10;
        setArgText("d","系数");

    }

    @Override
    public void Operator(VisionImage img, Context context) {
        Core.Light(img,d/10.0f);
    }

    @Override
    public String OperatorName() {
        return "明暗处理";
    }
}
