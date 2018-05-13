package us.xingkong.visionlibrary.operator;

import android.content.Context;

import vision.core.Operator;
import vision.core.Polygon;
import vision.image.VisionImage;

/**
 * Low Poly 低多边形
 *
 * Created by 饶翰新 on 2018/4/13.
 */

public class LowPoly extends ImageOperator {

    public int maxGra;
    public int initialSize;
    public int pc;
    public int rpc;


    public LowPoly(){
        super();
        maxGra = 30;
        pc = 2000;
        initialSize = 5000;
        rpc = 100;
        setArgText("pc","绘制点数");
        setArgText("rpc","随机点数");
        setArgText("initialSize","包围三角形大小");
        setArgText("maxGra","灰度阈值");
    }

    @Override
    public void Operator(VisionImage img, Context context) {
        Polygon.LowPolyOption op = new Polygon.LowPolyOption().GraMax(maxGra).PointCount(pc).RandomPointsCount(rpc).InitialSize(initialSize);
        Polygon.LowPoly(img, Operator.Laplacian,1,op);
    }

    @Override
    public String OperatorName() {
        return "Low Poly";
    }
}
