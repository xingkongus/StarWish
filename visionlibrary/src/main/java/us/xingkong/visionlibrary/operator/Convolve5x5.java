package us.xingkong.visionlibrary.operator;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve5x5;

import vision.image.VisionImage;

/**
 * 模糊
 *
 * Created by 饶翰新 on 2018/4/13.
 */
public class Convolve5x5 extends ImageOperator {

    protected float[][] kerlen;
    public int t;

    public Convolve5x5(){
        super();
        t = 1;
        setArgText("kerlen","卷积核");
        setArgText("t","迭代次数");
    }

    @Override
    public void Operator(VisionImage img, Context context) {

        if(kerlen == null);
        RenderScript script = RenderScript.create(context);
        Bitmap bm = Bitmap.createBitmap(img.getRGB(),img.getWidth(),img.getHeight(), Bitmap.Config.ARGB_8888);

        ScriptIntrinsicConvolve5x5 st = ScriptIntrinsicConvolve5x5.create(script,Element.U8_4(script));
        float[] v = new float[5*5];
        for(int i = 0;i < 5;i++)
            for(int j = 0;j < 5;j++)
                v[i*5 + j] = kerlen[i][j];
        st.setCoefficients(v);

        for(int i = 0;i < t;i++) {
            Allocation input = Allocation.createFromBitmap(script,bm);
            Allocation output = Allocation.createTyped(script,input.getType());
            st.setInput(input);
            st.forEach(output);

            output.copyTo(bm);
        }
        bm = bm.copy(Bitmap.Config.RGB_565,true);
        bm.getPixels(img.getRGB(),0,img.getWidth(),0,0,img.getWidth(),img.getHeight());
        st.destroy();

    }

    @Override
    public String OperatorName() {
        return "卷积";
    }
}
