package us.xingkong.visionlibrary.operator;

import android.content.Context;

import vision.core.Core;
import vision.image.VisionImage;

/**
 * 加权模糊
 *
 * Created by 饶翰新 on 2018/4/13.
 */

public class BlurEx extends ImageOperator {

    public int r;
    public int t;

    public BlurEx(){
        super();
        r = 2;
        t = 1;
        setArgText("r","模糊半径");
        setArgText("t","迭代次数");
    }

    @Override
    public void Operator(VisionImage img, Context context) {
        Core.BlurEx(img,r,t);
    }

    @Override
    public String OperatorName() {
        return "加权模糊";
    }
}
