package us.xingkong.visionlibrary.operator;

import android.content.Context;

import vision.core.Core;
import vision.image.VisionImage;

/**
 * 加权灰化
 *
 * Created by 饶翰新 on 2018/4/13.
 */

public class GreyEx extends ImageOperator {

    public GreyEx(){
        super();
    }

    @Override
    public void Operator(VisionImage img, Context context) {
        Core.GreyEx(img);
    }

    @Override
    public String OperatorName() {
        return "加权灰化";
    }
}
