package us.xingkong.starwishingbottle.module.wish;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.glide.transformations.BlurTransformation;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.module.info.InfoActivity;
import us.xingkong.starwishingbottle.util.GlideImageLoader;
import xyz.sealynn.bmobmodel.model.Message;
import xyz.sealynn.bmobmodel.model.Reversion;
import xyz.sealynn.bmobmodel.model.User;

public class WishingActivity extends AppCompatActivity {

    public static void showWishing(Context context,Message message){
        Intent intent = new Intent(context,WishingActivity.class);
        intent.putExtra("wishing",message.getObjectId());
        context.startActivity(intent);
    }

    private Message message;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private User owner,me;

    @BindView(R.id.head_image)
    AppCompatImageView headImg;
    @BindView(R.id.headPic)
    AppCompatImageView headPic;
    @BindView(R.id.picture)
    AppCompatImageView picture;
    @BindView(R.id.isprivate)
    AppCompatImageView isprivate;
    @BindView(R.id.isfinisher)
    AppCompatImageView isfinisher;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.user)
    AppCompatTextView tv_user;
    @BindView(R.id.preview)
    AppCompatTextView preview;
    @BindView(R.id.date)
    AppCompatTextView date;
    @BindView(R.id.part_user)
    LinearLayout part_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishing);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("愿望");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starOrUnstar();
            }
        });

        ButterKnife.bind(this);

        String wishing = getIntent().getStringExtra("wishing");
        if(wishing == null){
            init(null,new IllegalArgumentException("Wishing为空！"));
        }else {
            BmobQuery<Message> query = new BmobQuery<Message>();
            query.getObject(wishing, new QueryListener<Message>() {
                @Override
                public void done(Message message, BmobException e) {
                    init(message,e);
                }
            });
        }

        me = User.getCurrentUser(User.class);
    }

    protected void init(final Message message, Exception e){
        this.message = message;
        if(e != null){
            e.printStackTrace();
            Log.d(this.toString(),e.toString());
            return;
        }

        if(message == null)
            return;

        if(message.getPicture() != null && message.getPicture().getUrl() != null)
            Glide.with(this).load(message.getPicture().getUrl())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(13)))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(headImg);

        if(message.getUser() != null)
        {
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(message.getUser().getObjectId(), new QueryListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                if(e != null){
                    e.printStackTrace();
                    Log.d("show Wishing",e.toString());
                }else if(user == null){
                    Log.d("show Wishing","User is null");
                }else{
                    owner = user;
                    refresh();
                }
                }
            });
            part_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InfoActivity.showUserInfo(WishingActivity.this,message.getUser());
                }
            });
        }

        if(message.getFinished() == null)
            isfinisher.setVisibility(View.GONE);
        if(message.getPublished())
            isprivate.setVisibility(View.GONE);

        if (message.getPicture() != null && message.getPicture().getUrl() != null) {

            Glide.with(this)
                    .load(message.getPicture().getUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_action_picture).error(R.drawable.ic_action_picture))
                    .into(GlideImageLoader.FitXY(picture,R.id.action_settings));
        }

        preview.setText(message.getContent());
        date.setText(message.getUpdatedAt());
        updateFab();
    }

    protected void refresh(){
        if(owner == null)
            return;
        User user = owner;
        String username = user.getNicknameOrUsername();
        toolbarLayout.setTitle(username + "的愿望");

        if (owner != null && user.getAvatar() != null && user.getAvatar().getUrl() != null)
            GlideImageLoader.Circle(Glide.with(this)
                    .load(user.getAvatar().getUrl()))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_action_person))
                    .into(headPic);
        else
            GlideImageLoader.Circle(Glide.with(this)
                    .load(R.drawable.ic_action_person)
                    .transition(new DrawableTransitionOptions().crossFade()))
                    .into(headPic);

        tv_user.setText(username);
    }

    protected void starOrUnstar(){
        fab.setEnabled(false);
        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("user",me);
        query.addWhereEqualTo("message",message);
        query.setLimit(1);
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(List<Reversion> list, BmobException e) {
                if(e != null){
                    e.printStackTrace();
                    Log.d(this.toString(),e.toString());
                    fab.setEnabled(true);
                }else if(list != null && list.size() > 0 ){
                    list.get(0).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e != null){
                                e.printStackTrace();
                                Log.d(this.toString(),e.toString());
                            }
                            updateFab();
                        }
                    });

                }else {
                    Reversion reversion = new Reversion();
                    reversion.setMessage(message);
                    reversion.setUser(me);
                    reversion.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e != null){
                                e.printStackTrace();
                                Log.d(this.toString(),e.toString());
                            }
                            updateFab();
                        }
                    });
                }
            }
        });

    }

    protected void updateFab(){
        if(message == null || me == null)
            return;
        fab.setEnabled(false);

        if(message.getUser().getObjectId().equals(me.getObjectId())){
            fab.setVisibility(View.GONE);
            return;
        } else
            fab.setVisibility(View.VISIBLE);

        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("user",me);
        query.addWhereEqualTo("message",message);
        query.setLimit(1);
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(List<Reversion> list, BmobException e) {
                if(e != null){
                    e.printStackTrace();
                    Log.d(this.toString(),e.toString());
                }else if(list != null && list.size() > 0 ){
                    fab.setImageResource(R.drawable.ic_action_like);
                }else {
                    fab.setImageResource(R.drawable.ic_action_unlike);
                }
                fab.setEnabled(true);
            }
        });


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
