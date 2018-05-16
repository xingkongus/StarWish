package us.xingkong.starwishingbottle.module.wish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.adapter.RecyclerAdapterOther;
import us.xingkong.starwishingbottle.base.Constants;
import us.xingkong.starwishingbottle.dialog.DoItDialog;
import us.xingkong.starwishingbottle.dialog.GetPictureDialog;
import us.xingkong.starwishingbottle.dialog.ShareDIalog;
import us.xingkong.starwishingbottle.module.editmsg.EditMsgActivity;
import us.xingkong.starwishingbottle.module.info.InfoActivity;
import us.xingkong.starwishingbottle.util.GlideImageLoader;
import xyz.sealynn.bmobmodel.model.Message;
import xyz.sealynn.bmobmodel.model.Reversion;
import xyz.sealynn.bmobmodel.model.User;

public class WishingActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    public static void showWishing(Context context, Message message, String isFinishedID) {
        Intent intent = new Intent(context, WishingActivity.class);
        intent.putExtra("wishing", message.getObjectId());
        intent.putExtra("isfinished", isFinishedID);
        context.startActivity(intent);
    }

    private Message message;
    private Boolean isFinished;
    private String isFinishedID;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private User owner, me;
    private DoItDialog doItDialog;
    private ProgressDialog progressDialog;
    private RecyclerAdapterOther adapter;
    private List<Reversion> data;
    private User bestUser;

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
    @BindView(R.id.do_it)
    AppCompatButton doIt;

    @BindView(R.id.wishing_other)
    RecyclerView recyclerView;

    @BindView(R.id.user_best)
    AppCompatTextView tv_user_best;
    @BindView(R.id.headPic_best)
    AppCompatImageView headPicBest;
    @BindView(R.id.part_user_best)
    LinearLayout part_user_best;
    @BindView(R.id.preview_best)
    AppCompatTextView previewBest;
    @BindView(R.id.part_best)
    LinearLayout part_best;
    @BindView(R.id.date_best)
    AppCompatTextView dateBest;
    @BindView(R.id.picture_best)
    AppCompatImageView pictureBest;
    @BindView(R.id.none)
    AppCompatTextView none;

    private MenuItem delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishing);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("愿望");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Log.d("ctionBar", String.valueOf(actionBar == null));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                starOrUnstar();
            }
        });

        ButterKnife.bind(this);

        doIt.setVisibility(View.GONE);

        String wishing = getIntent().getStringExtra("wishing");
        isFinishedID = getIntent().getStringExtra("isfinished");
        if (isFinishedID != null && isFinishedID.length() > 0)
            isFinished = true;
        else
            isFinished = false;

        if (!isFinished) {
            part_best.setVisibility(View.GONE);
        }
        if (wishing == null) {
            init(null, new IllegalArgumentException("Wishing为空！"));
        } else {
            BmobQuery<Message> query = new BmobQuery<Message>();
            query.getObject(wishing, new QueryListener<Message>() {
                @Override
                public void done(Message message, BmobException e) {
                    init(message, e);
                }
            });
        }

        me = User.getCurrentUser(User.class);

    }

    protected void init(final Message message, Exception e) {
        this.message = message;
        if (e != null) {
            e.printStackTrace();
            Log.d(this.toString(), e.toString());
            return;
        }

        if (message == null)
            return;

        if (message.getPicture() != null && message.getPicture().getUrl() != null)
            Glide.with(this).load(message.getPicture().getUrl())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(13)))
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(headImg);

        if (message.getUser() != null) {
            BmobQuery<User> query = new BmobQuery<>();
            query.getObject(message.getUser().getObjectId(), new QueryListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    if (e != null) {
                        e.printStackTrace();
                        Log.d("show Wishing", e.toString());
                    } else if (user == null) {
                        Log.d("show Wishing", "User is null");
                    } else {
                        owner = user;
                        refresh();
                    }
                }
            });
            part_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InfoActivity.showUserInfo(WishingActivity.this, message.getUser());
                }
            });
        }

        if (!message.isFinished())
            isfinisher.setVisibility(View.GONE);
        if (message.getPublished())
            isprivate.setVisibility(View.GONE);

        if (message.getPicture() != null && message.getPicture().getUrl() != null) {
            Glide.with(this)
                    .load(message.getPicture().getUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_action_picture).error(R.drawable.ic_action_picture))
                    .into(GlideImageLoader.FitXY(picture, R.id.action_settings));
        }
        progressDialog = new ProgressDialog(WishingActivity.this);
        progressDialog.setTitle("正在提交");
        progressDialog.setMessage("请稍等…");
        progressDialog.setCancelable(false);

        doItDialog = new DoItDialog(this);
        doItDialog.setResultListener(new DoItDialog.ResultListener() {
            @Override
            public void onOK(File imageFile, String msg) {

                progressDialog.show();
                Reversion.submit(me, message, msg, imageFile, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e != null) {
                            e.printStackTrace();
                            Toast.makeText(WishingActivity.this, "提交失败\n" + e.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WishingActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        }
                        updateDoit();
                        updateFab();
                        refreshOther();
                        initBest();
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onSelectPicture() {
                GetPictureDialog.GetPicture(WishingActivity.this, 1, 2, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doItDialog.setImageFile(null);
                    }
                });
            }
        });


        doIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((!EasyPermissions.hasPermissions(WishingActivity.this, Constants.PERMISSIONS_EXTERNAL_STORAGE))
                        && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    EasyPermissions.requestPermissions(WishingActivity.this, getString(R.string.need_permission),
                            0, Constants.PERMISSIONS_EXTERNAL_STORAGE);
                } else {
                    doItDialog.show();
                }
            }
        });
        preview.setText(message.getContent());
        date.setText(message.getUpdatedAt());


        initBest();
        initOther();
        updateFab();
        updateDoit();
        updateDelete();
    }

    void initBest() {
        if (this.message == null || this.message.getFinished() == null || message.getFinished().getObjectId() == null || message.getFinished().getObjectId().length() == 0) {
            return;
        }
        BmobQuery<Reversion> query = new BmobQuery<>();
        query.getObject(message.getFinished().getObjectId(), new QueryListener<Reversion>() {
            @Override
            public void done(final Reversion reversion, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.d("initBest", e.toString());
                } else if (reversion != null) {

                    setBest(reversion);
                } else {
                    part_best.setVisibility(View.GONE);
                }
            }
        });
    }

    void setBest(final Reversion reversion) {
        part_best.setVisibility(View.VISIBLE);
        previewBest.setText(reversion.getContent());
        dateBest.setText(reversion.getUpdatedAt());
        part_best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoActivity.showUserInfo(WishingActivity.this, reversion.getUser());
            }
        });
        if (reversion.getPicture() != null && reversion.getPicture().getUrl() != null) {
            Glide.with(WishingActivity.this)
                    .load(reversion.getPicture().getUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_action_picture).error(R.drawable.ic_action_picture))
                    .into(GlideImageLoader.FitXY(pictureBest, R.id.action_settings));
        }
        BmobQuery<User> q = new BmobQuery<>();
        q.getObject(reversion.getUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.d(this.toString(), e.toString());
                    WishingActivity.this.bestUser = null;
                } else {
                    WishingActivity.this.bestUser = user;
                    tv_user_best.setText(user.getNicknameOrUsername());
                    if (user != null && user.getAvatar() != null && user.getAvatar().getUrl() != null)
                        GlideImageLoader.Circle(Glide.with(WishingActivity.this)
                                .load(user.getAvatar().getUrl()))
                                .transition(new DrawableTransitionOptions().crossFade())
                                .apply(new RequestOptions().placeholder(R.drawable.ic_action_person))
                                .into(headPicBest);
                    else
                        GlideImageLoader.Circle(Glide.with(WishingActivity.this)
                                .load(R.drawable.ic_action_person)
                                .transition(new DrawableTransitionOptions().crossFade()))
                                .into(headPicBest);
                }
            }
        });

    }

    void initOther() {

        if (message == null)
            return;

        data = new ArrayList<>();
        adapter = new RecyclerAdapterOther(data, this, new RecyclerAdapterOther.Like() {
            @Override
            public void iLikeIt(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WishingActivity.this);
                builder.setTitle("确定喜欢它吗？");
                builder.setMessage("将其标记为最佳的愿望实现后，不能再标记其他了哦~");
                builder.setIcon(R.drawable.ic_action_like_pnik);
                builder.setNegativeButton("让我再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerView.setEnabled(false);
                        Reversion re = data.get(position);
                        message.setFinished(re);
                        message.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    setBest(message.getFinished());
                                    refreshOther();
                                } else {
                                    e.printStackTrace();
                                    Log.d("iLikeIt", e.toString());
                                    Toast.makeText(WishingActivity.this, "标记失败\n" + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                                recyclerView.setEnabled(true);
                            }
                        });
                    }
                });
                builder.show();
            }

            @Override
            public boolean shouldShowLike(int position) {
                return owner != null && me != null && owner.getObjectId().equals(me.getObjectId()) && !message.isFinished();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        refreshOther();
    }

    void refreshOther() {
        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("message", message);
        query.addWhereEqualTo("finished", true);
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(List<Reversion> list, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.d("initOther", e.toString());
                    return;
                }
                if (list != null && list.size() > 0) {
                    if (isFinished) {

                        for (int i = 0; i < list.size(); i++) {
                            // 移除最佳
                            if (list.get(i).getObjectId().equals(isFinishedID)) {
                                list.remove(i);
                                break;
                            }
                        }
                    }

                    data = list;
                    adapter.setMessages(list);
                    adapter.notifyDataSetChanged();
                    if (list.size() == 0)
                        none.setVisibility(View.VISIBLE);
                    else
                        none.setVisibility(View.GONE);
                }
            }
        });
    }

    protected void refresh() {
        if (owner == null)
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

    protected void starOrUnstar() {
        fab.setEnabled(false);
        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("user", me);
        query.addWhereEqualTo("message", message);
        query.setLimit(1);
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(List<Reversion> list, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.d(this.toString(), e.toString());
                    fab.setEnabled(true);
                } else if (list != null && list.size() > 0) {
                    if (list.get(0).getFinished()) {
                        updateFab();
                        return;
                    }
                    list.get(0).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                e.printStackTrace();
                                Log.d(this.toString(), e.toString());
                            }
                            updateFab();
                        }
                    });

                } else {
                    Reversion reversion = new Reversion();
                    reversion.setMessage(message);
                    reversion.setUser(me);
                    reversion.setFinished(false);
                    reversion.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e != null) {
                                e.printStackTrace();
                                Log.d(this.toString(), e.toString());
                            }
                            updateFab();
                        }
                    });
                }
            }
        });

    }

    protected void updateDelete() {
        if (message == null || me == null)
            return;

        if (message.getUser().getObjectId().equals(me.getObjectId())) {
            delete.setVisible(true);
        } else {
            delete.setVisible(false);
        }
    }

    protected void updateDoit() {
        if (message == null || me == null)
            return;

        if (message.getUser().getObjectId().equals(me.getObjectId())) {
            doIt.setVisibility(View.GONE);
            return;
        }
        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("message", message);
        query.addWhereEqualTo("user", me);
        query.setLimit(1);
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(List<Reversion> list, BmobException e) {
                if (e != null) {
                    e.printStackTrace();
                    Log.d("updateDoit", e.toString());
                } else if (list.size() > 0 && list.get(0).getFinished()) {
                    doIt.setVisibility(View.GONE);
                } else {
                    doIt.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected void updateFab() {
        if (message == null || me == null)
            return;
        fab.setEnabled(false);

        if (message.getUser().getObjectId().equals(me.getObjectId())) {
            fab.setVisibility(View.GONE);
            return;
        } else {
            fab.setVisibility(View.VISIBLE);
        }

        BmobQuery<Reversion> query = new BmobQuery<>();
        query.addWhereEqualTo("user", me);
        query.addWhereEqualTo("message", message);
        query.setLimit(1);
        query.findObjects(new FindListener<Reversion>() {
            @Override
            public void done(List<Reversion> list, BmobException e) {
                fab.setEnabled(true);
                if (e != null) {
                    e.printStackTrace();
                    Log.d(this.toString(), e.toString());
                } else if (list != null && list.size() > 0) {
                    fab.setImageResource(R.drawable.ic_action_like);
                    if (message.isFinished() && bestUser != null && bestUser.getObjectId().equals(me.getObjectId()))
                        fab.setEnabled(false);
                } else {
                    fab.setImageResource(R.drawable.ic_action_unlike);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        delete = menu.findItem(R.id.delete);
        delete.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.shared:
                if (message != null && message.getContent() != null && owner != null) {
                    if ((!EasyPermissions.hasPermissions(WishingActivity.this, Constants.PERMISSIONS_EXTERNAL_STORAGE))
                            && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        EasyPermissions.requestPermissions(WishingActivity.this, getString(R.string.need_permission),
                                0, Constants.PERMISSIONS_EXTERNAL_STORAGE);
                    } else
                        ShareDIalog.share(WishingActivity.this, message, owner);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "正在载入，请稍后再试", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (data != null && data.getData() != null) {
                doItDialog.setImageUri(data.getData());
            }
        } else if (requestCode == 2) {
            if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                File file = new File(getExternalCacheDir(), "camera.jpg");
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("?", "???");
                }

                doItDialog.setImageFile(file);
            }
        }
    }

    /**
     * 以下是关于EasyPermissions对权限的操作
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

}
