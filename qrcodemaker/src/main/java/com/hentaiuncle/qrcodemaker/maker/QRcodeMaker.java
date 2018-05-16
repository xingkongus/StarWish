package com.hentaiuncle.qrcodemaker.maker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.TextPaint;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QRcodeMaker {


    protected String content;
    protected int width,height;
    protected QRCodeWriter writer;

    protected String title;
    Bitmap logo;
    protected int frontColor = Color.rgb(100,100,200);
    protected int backgroundColor = 0xffffffff;

    public QRcodeMaker(String content){
        writer = new QRCodeWriter();
        width = 200;
        height = 200;
        this.content = content;
    }

    public void setFrontColor(int frontColor) {
        this.frontColor = frontColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getFrontColor() {
        return frontColor;
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap make() throws WriterException {
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        BitMatrix encode = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        int[] pixels = new int[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (encode.get(j, i)) {
                    pixels[i * width + j] = frontColor;
                } else {
                    pixels[i * width + j] = backgroundColor;
                }
            }
        }
        Bitmap code = Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        Bitmap tmp = Bitmap.createBitmap(code.getWidth(),code.getHeight() + 100, Bitmap.Config.RGB_565);


        Canvas canvas = new Canvas(tmp);

        if(logo != null){
            Paint paint = new Paint();

            paint.setColor(backgroundColor);


            int w = code.getWidth() / 10;
            int x = code.getWidth() / 2 - w / 2;
            int y = (tmp.getHeight() - code.getHeight()) - 30;

            double radius = Math.sqrt(2 * (w/2) * (w/2));

            canvas.drawRect(0,0,tmp.getWidth(),tmp.getHeight(),paint);
            canvas.drawBitmap(code,0,y,paint);
            canvas.drawCircle(code.getWidth() / 2 ,y + code.getHeight()/2, (float) radius,paint);
            canvas.drawBitmap(logo
                    ,new Rect(0,0,logo.getWidth(),logo.getHeight())
                    ,new Rect(x,y+ code.getHeight()/2-w/2 ,x + w,y + code.getHeight()/2 + w/2)
                    ,paint);
        }

        if(title != null) {
            // 创建画笔 Paint
            Paint textPaint = new Paint();
            // 设置颜色
            textPaint.setColor(Color.BLACK);
            // 设置样式
            textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            textPaint.setTextSize(50);

            canvas.drawText(title,60,80,textPaint);
        }
        canvas.save();
        return  tmp;

    }

    public void make(AsyncCallback asyncCallback){
        MakerTask makerTask = new MakerTask(asyncCallback);
        makerTask.execute();
    }

    class MakerTask extends AsyncTask<Void, Void, Bitmap> {

        AsyncCallback asyncCallback;
        Exception exception;

        MakerTask (AsyncCallback asyncCallback){
            this.asyncCallback = asyncCallback;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                return QRcodeMaker.this.make();
            } catch (WriterException e) {
                e.printStackTrace();
                Log.d("MakerTask",e.toString());
                exception = e;
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            asyncCallback.onStart();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            asyncCallback.onDone(bitmap,exception);
        }
    };

    public interface AsyncCallback {
        void onStart();
        void onDone(Bitmap bitmap, Exception e);
    }
}
