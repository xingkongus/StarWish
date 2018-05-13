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

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.starwishingbottle.R;
import xyz.sealynn.bmobmodel.model.Message;

import static android.support.constraint.Constraints.TAG;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        Log.d(TAG, "++++++++++++++++" + message.getContent());
        holder.preview.setText(message.getContent());
        if (message.getPicture() != null)
            Glide.with(context).load(message.getPicture().getUrl()).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.card_view)
        CardView cardView;
        //        @BindView(R.id.picture)
        AppCompatImageView picture;
        //        @BindView(R.id.preview)
        AppCompatTextView preview;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            preview = itemView.findViewById(R.id.preview);
            picture = itemView.findViewById(R.id.picture);
        }
    }
}
