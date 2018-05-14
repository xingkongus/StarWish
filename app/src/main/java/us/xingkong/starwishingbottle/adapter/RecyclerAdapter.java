package us.xingkong.starwishingbottle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.module.info.InfoActivity;
import us.xingkong.starwishingbottle.util.GlideImageLoader;
import xyz.sealynn.bmobmodel.model.Message;
import xyz.sealynn.bmobmodel.model.User;

/**
 * Created by SeaLynn0 on 2018/5/13 18:38
 * <p>
 * Email：sealynndev@gmail.com
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;

    private List<Message> messages;

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public RecyclerAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_wish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Message message = messages.get(position);

        holder.preview.setText(message.getContent());
        if (message.getPicture() != null && message.getPicture().getUrl() != null)
            Glide.with(context)
                    .load(message.getPicture().getUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.blowball_dandelion_dandelion_seed_54300))
                    .into(holder.picture);
        else
            Glide.with(context)
                    .load(R.drawable.blowball_dandelion_dandelion_seed_54300)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(holder.picture);

        BmobQuery<User> q = new BmobQuery<>();

        q.getObject(message.getUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e != null){
                    e.printStackTrace();
                    Log.d(this.toString(),e.toString());
                }else{
                    holder.user.setText(user.getUsername());

                    if (user != null && user.getAvatar() != null && user.getAvatar().getUrl() != null)
                        GlideImageLoader.Circle(Glide.with(context)
                                .load(user.getAvatar().getUrl()))
                                .transition(new DrawableTransitionOptions().crossFade())
                                .apply(new RequestOptions().placeholder(R.drawable.ic_action_person))
                                .into(holder.headPic);
                    else
                        GlideImageLoader.Circle(Glide.with(context)
                                .load(R.drawable.ic_action_person)
                                .transition(new DrawableTransitionOptions().crossFade()))
                                .into(holder.headPic);
                }
            }
        });

        if(message.getFinished() == null)
            holder.isFinished.setVisibility(View.GONE);
        else
            holder.isFinished.setVisibility(View.VISIBLE);

        if(message.getPublished())
            holder.isPrivate.setVisibility(View.GONE);
        else
            holder.isPrivate.setVisibility(View.VISIBLE);

        holder.date.setText(message.getCreatedAt());
        holder.userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoActivity.showUserInfo(context,message.getUser());
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        AppCompatImageView picture,headPic,isFinished,isPrivate;
        AppCompatTextView preview,user,date;
        LinearLayout userinfo;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            preview = itemView.findViewById(R.id.preview);
            user = itemView.findViewById(R.id.user);
            date = itemView.findViewById(R.id.date);

            picture = itemView.findViewById(R.id.picture);
            headPic = itemView.findViewById(R.id.headPic);
            isFinished = itemView.findViewById(R.id.isfinisher);
            isPrivate = itemView.findViewById(R.id.isprivate);

            userinfo = itemView.findViewById(R.id.part_user);
        }
    }
}
