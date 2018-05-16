package com.hentaiuncle.qrcodemaker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.hentaiuncle.qrcodemaker.maker.QRcodeMaker;

public class ScrollingActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        imageView = findViewById(R.id.qrcode);

        QRcodeMaker maker = new QRcodeMaker("asdasdasdas~");
        maker.setWidth(500);
        maker.setHeight(500);
        maker.setTitle("xxxxx");
        maker.setLogo(BitmapFactory.decodeResource(getResources(),R.drawable.like));
        maker.make(new QRcodeMaker.AsyncCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onDone(Bitmap bitmap, Exception e) {
                if(e == null && bitmap != null){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }
}
