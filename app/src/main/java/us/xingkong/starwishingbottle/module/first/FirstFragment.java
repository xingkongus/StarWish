package us.xingkong.starwishingbottle.module.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.starwishingbottle.util.GlideImageLoader;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseFragment;
import us.xingkong.starwishingbottle.module.login.LoginActivity;
import us.xingkong.starwishingbottle.module.register.RegisterActivity;

/**
 * Created by SeaLynn0 on 2018/4/24 18:04
 * <p>
 * Email：sealynndev@gmail.com
 */
public class FirstFragment extends BaseFragment<FirstContract.Presenter>
        implements FirstContract.View {

    @BindView(R.id.bt_start)
    AppCompatButton login;
    @BindView(R.id.bt_regist)
    AppCompatButton register;

    Banner banner;

    List<Integer> list;

    @Override
    protected void init(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        int[] imageResIDs = {R.drawable.blowball_dandelion_dandelion_seed_54300,
                R.drawable.blowball_dandelion_dandelion_seed_54300,
                R.drawable.blowball_dandelion_dandelion_seed_54300,
                R.drawable.blowball_dandelion_dandelion_seed_54300};
        list = new ArrayList<>();

        for (int imageResID : imageResIDs) {
            list.add(imageResID);
        }

        banner = root.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(list);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getFragmentContext(), LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getFragmentContext(), RegisterActivity.class));
            }
        });
    }

    @Override
    protected FirstContract.Presenter createPresenter() {
        return new FirstPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_first;
    }

}
