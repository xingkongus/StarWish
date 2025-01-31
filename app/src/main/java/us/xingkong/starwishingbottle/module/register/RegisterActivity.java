package us.xingkong.starwishingbottle.module.register;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import butterknife.BindView;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;

/**
 * Created by SeaLynn0 on 2018/4/23 23:46
 * <p>
 * Email：sealynndev@gmail.com
 */
public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {

    @BindView(R.id.bt_regist)
    AppCompatButton register;
    @BindView(R.id.et_username)
    AppCompatEditText username;
    @BindView(R.id.et_password)
    AppCompatEditText password;
    @BindView(R.id.et_password_re)
    AppCompatEditText passwordRe;
    @BindView(R.id.et_nickname)
    AppCompatEditText nickname;
    @BindView(R.id.back)
    AppCompatImageButton back;


    @Override
    protected void initEvent(Bundle savedInstanceState) {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.register(username.getText().toString().trim(),
                        nickname.getText().toString().trim(),
                        passwordRe.getText().toString().trim(),
                        password.getText().toString().trim(),RegisterActivity.this.getIntent().getData());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void prepareData() {
    }

    @Override
    protected RegisterContract.Presenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void freeze() {
        register.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);
        passwordRe.setEnabled(false);
        nickname.setEnabled(false);
    }

    @Override
    public void unfreeze() {
        register.setEnabled(true);
        username.setEnabled(true);
        password.setEnabled(true);
        passwordRe.setEnabled(true);
        nickname.setEnabled(true);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}
