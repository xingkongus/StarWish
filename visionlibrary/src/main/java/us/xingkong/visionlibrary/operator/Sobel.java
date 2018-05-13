package us.xingkong.visionlibrary.operator;

import android.content.Context;

import vision.core.Core;
import vision.core.Operator;
import vision.image.VisionImage;

/**
 * Sobel边缘检测
 * <p>
 * Created by 饶翰新 on 2018/4/13.
 */

public class Sobel extends ImageOperator {

    public Sobel() {
        super();
    }

    @Override
    public void Operator(VisionImage img, Context context) {
        Convolve3x3 c1, c2;
        VisionImage img1 = img.Copy(), img2 = img.Copy();
        c1 = new Convolve3x3();
        c2 = new Convolve3x3();
        c1.kerlen = Operator.Sobel_X;
        c2.kerlen = Operator.Sobel_Y;

        c1.Operator(img1, context);
        c2.Operator(img2, context);

        int[] RGBX = img1.getRGB(), RGBY = img2.getRGB(), RGB = img.getRGB(), rgbX, rgbY;
        int x, y, index, W = img.getWidth(), H = img.getHeight(), r, g, b, a;

        for (y = 0; y < H; y++) {
            for (x = 0; x < W; x++) {
                index = y * W + x;
                rgbX = Core.RgbSplit(RGBX[index]);
                rgbY = Core.RgbSplit(RGBY[index]);
                r = (int) Math.sqrt(rgbX[0] * rgbX[0] + rgbY[0] * rgbY[0]);
                g = (int) Math.sqrt(rgbX[1] * rgbX[1] + rgbY[1] * rgbY[1]);
                b = (int) Math.sqrt(rgbX[2] * rgbX[2] + rgbY[2] * rgbY[2]);
                a = (int) Math.sqrt(rgbX[3] * rgbX[3] + rgbY[3] * rgbY[3]);
                if (r < 0)
                    r = 0;
                if (r > 255)
                    r = 255;
                if (g < 0)
                    g = 0;
                if (g > 255)
                    g = 255;
                if (b < 0)
                    b = 0;
                if (b > 255)
                    b = 255;
                if (a < 0)
                    a = 0;
                if (a > 255)
                    a = 255;
                RGB[index] = Core.RGB(r,g,b,a);
            }
        }
    }

    @Override
    public String OperatorName() {
        return "Sobel边缘检测";
    }
}
