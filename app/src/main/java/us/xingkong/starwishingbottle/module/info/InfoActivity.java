package us.xingkong.starwishingbottle.module.info;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.module.main.MainActivity;
import us.xingkong.starwishingbottle.module.setting.SettingActivity;
import us.xingkong.starwishingbottle.util.GlideImageLoader;
import xyz.sealynn.bmobmodel.model.User;

/**
 * 用户信息
 */
public class InfoActivity extends AppCompatActivity {


    //--------------------------------------------------------------------
    /**
     * 静态变量以及方法
     */
    public static final String IntentKey_UserID = "userID";

    public static void showUserInfo(Context packageContext,User user){
        Intent intent = new Intent(packageContext,InfoActivity.class);
        intent.putExtra(IntentKey_UserID,user.getObjectId());
        packageContext.startActivity(intent);
    }

    public static void showUserInfo(Context packageContext,String userID){
        Intent intent = new Intent(packageContext,InfoActivity.class);
        intent.putExtra(IntentKey_UserID,userID);
        packageContext.startActivity(intent);
    }
    //--------------------------------------------------------------------


    private Toolbar toolbar;
    private AppCompatImageView headImg,headPic;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        initView();

        String id = getIntent().getStringExtra(IntentKey_UserID);

        if(id == null){
            init(null,new IllegalArgumentException("用户ID为空！"));
        }else {
            BmobQuery<User> query = new BmobQuery<User>();
            query.getObject(id, new QueryListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    init(user,e);
                }
            });
        }
    }

    protected void initView(){
        toolbar = findViewById(R.id.toolbar);
        headImg = findViewById(R.id.head_image);
        headPic = findViewById(R.id.headPic);
        toolbarLayout = findViewById(R.id.collapsing_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void init(User user,Exception e){
        if(e != null){
            e.printStackTrace();
            Log.d(this.toString(),e.toString());
            return;
        }

        if(user == null)
            return;

        toolbarLayout.setTitle(user.getUsername());
        if(user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar().getUrl()).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(headImg);
            GlideImageLoader.Circle(Glide.with(this).load(user.getAvatar().getUrl())).
                    into(headPic);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
