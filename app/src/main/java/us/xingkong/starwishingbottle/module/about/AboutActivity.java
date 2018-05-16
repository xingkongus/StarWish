package us.xingkong.starwishingbottle.module.about;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.util.VersionCodeUtil;

/**
 * Created by SeaLynn0 on 2018/5/14 20:36
 * <p>
 * Email：sealynndev@gmail.com
 */
public class AboutActivity extends MaterialAboutActivity {
    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {

        MaterialAboutCard author = new MaterialAboutCard.Builder()
                .title("Author")
                .addItem(new MaterialAboutActionItem.Builder()
                        .text("@SeaLynn0").build())
                .addItem(new MaterialAboutActionItem.Builder()
                        .text("@Hansin").build())
                .build();

        MaterialAboutCard version = new MaterialAboutCard.Builder()
                .title("AppInfo")
                .addItem(new MaterialAboutActionItem.Builder().text("VersionCode")
                        .subText(VersionCodeUtil.getVersionCode(this) + "").build())
                .addItem(new MaterialAboutActionItem.Builder().text("VersionName")
                        .subText(VersionCodeUtil.getVersionName(this)).build())
                .build();

        MaterialAboutCard duty = new MaterialAboutCard.Builder()
                .title("免责声明")
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.duty_text).build())
                .build();

        return new MaterialAboutList.Builder()
                .addCard(version)
                .addCard(author)
                .addCard(duty)
                .build(); // This creates an empty screen, add cards with .addCard()
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.about);
    }
}
