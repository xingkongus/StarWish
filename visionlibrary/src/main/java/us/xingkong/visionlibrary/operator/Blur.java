package us.xingkong.visionlibrary.operator;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import vision.image.VisionImage;

/**
 * 模糊
 *
 * Created by 饶翰新 on 2018/4/13.
 */
public class Blur extends ImageOperator {

    public int r;
    public int t;

    public Blur(){
        super();
        r = 2;
        t = 1;
        setArgText("r","模糊半径");
        setArgText("t","迭代次数");

    }

    @Override
    public void Operator(VisionImage img, Context context) {
        if(r <= 0)
            return;
        RenderScript script = RenderScript.create(context);
        Bitmap bm = Bitmap.createBitmap(img.getRGB(),img.getWidth(),img.getHeight(), Bitmap.Config.ARGB_8888);

        ScriptIntrinsicBlur st = ScriptIntrinsicBlur.create(script, Element.U8_4(script));

        st.setRadius(r);

        for(int i = 0;i < t;i++) {
            Allocation input = Allocation.createFromBitmap(script,bm);
            Allocation output = Allocation.createTyped(script,input.getType());
            st.setInput(input);
            st.forEach(output);

            output.copyTo(bm);
        }

        bm.getPixels(img.getRGB(),0,img.getWidth(),0,0,img.getWidth(),img.getHeight());
        st.destroy();

    }

    @Override
    public String OperatorName() {
        return "模糊";
    }
}
