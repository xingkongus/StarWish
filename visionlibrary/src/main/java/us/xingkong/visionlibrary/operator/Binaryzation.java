package us.xingkong.visionlibrary.operator;

import android.content.Context;

import vision.core.Core;
import vision.image.VisionImage;

/**
 * 二值分(灰度)
 *
 * Created by 饶翰新 on 2018/4/13.
 */

public class Binaryzation extends ImageOperator {

    public int d;

    public Binaryzation(){
        super();
        d = 150;
        setArgText("d","阈值");
    }

    @Override
    public void Operator(VisionImage img, Context context) {
        Core.Binaryzation(img,d);
    }

    @Override
    public String OperatorName() {
        return "二值分（灰度）";
    }
}
