package us.xingkong.visionlibrary.operator;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;


import us.xingkong.visionlibrary.ScriptC_grey;
import vision.image.VisionImage;

/**
 * 灰化
 *
 * Created by 饶翰新 on 2018/4/13.
 */

public class Grey extends ImageOperator {

    public Grey(){
        super();

    }

    @Override
    public void Operator(VisionImage img, Context context) {
        RenderScript script = RenderScript.create(context);
        Bitmap bm = Bitmap.createBitmap(img.getRGB(),img.getWidth(),img.getHeight(), Bitmap.Config.ARGB_8888);

        ScriptC_grey st = new ScriptC_grey(script);
        Allocation input = Allocation.createFromBitmap(script,bm);
        Allocation output = Allocation.createTyped(script,input.getType());
        st.forEach_root(input,output);
        output.copyTo(bm);

        bm.getPixels(img.getRGB(),0,img.getWidth(),0,0,img.getWidth(),img.getHeight());
        st.destroy();

    }

    @Override
    public String OperatorName() {
        return "灰化";
    }
}
