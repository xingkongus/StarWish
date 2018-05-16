package us.xingkong.starwishingbottle.module.login;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import butterknife.BindView;
import us.xingkong.starwishingbottle.R;
import us.xingkong.starwishingbottle.base.BaseActivity;

/**
 * Created by SeaLynn0 on 2018/4/23 23:39
 * <p>
 * Email：sealynndev@gmail.com
 */
public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    @BindView(R.id.bt_login)
    AppCompatButton login;
    @BindView(R.id.et_username)
    AppCompatEditText username;
    @BindView(R.id.et_password)
    AppCompatEditText password;
    @BindView(R.id.back)
    AppCompatImageButton back;

    @Override
    protected void initEvent(Bundle savedInstanceState) {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.login(username.getText().toString().trim(),
                        password.getText().toString().trim(),LoginActivity.this.getIntent().getData());
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
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void freeze() {
        login.setEnabled(false);
        username.setEnabled(false);
        password.setEnabled(false);
    }

    @Override
    public void unfreeze() {
        login.setEnabled(true);
        username.setEnabled(true);
        password.setEnabled(true);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}
