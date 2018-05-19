package us.xingkong.starwishingbottle.module.browse;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;

/**
 * @作者: Xuer
 * @创建时间: 2018/5/19 0:01
 * @描述: 带过渡动画的图片浏览器，不过好像有时候图片会突然变得很大，然后正常。
 * 懒得做获取加载进度了，也许还可以做个保存功能？
 * @更新日志:
 */
public class BrowseActivity extends BaseActivity<BrowseContract.Presenter>
        implements BrowseContract.View {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.photoView)
    PhotoView photoView;

    @Override
    protected void initEvent(Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        finish();
        overridePendingTransition(0, R.anim.activity_exit);
    }

    @Override
    protected void prepareData() {
        Intent intent = getIntent();
        progressBar.setVisibility(View.VISIBLE);
        photoView.setEnabled(false);
        Glide.with(BrowseActivity.this)
                .load(intent.getStringExtra("Url"))
                .thumbnail(0.3f)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.drawable.ic_action_picture).error(R.drawable.ic_action_picture))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        photoView.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(photoView);
    }

    @Override
    protected BrowseContract.Presenter createPresenter() {
        return new BrowsePresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_browse;
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }
}
