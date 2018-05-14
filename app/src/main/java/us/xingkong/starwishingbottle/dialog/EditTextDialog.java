package us.xingkong.starwishingbottle.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import us.xingkong.starwishingbottle.R;

/**
 * 编辑文字对话框
 */
public class EditTextDialog extends AppCompatDialog {

    private String t;
    private String v;
    private String k;
    private EditResult result;

    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.key)
    AppCompatTextView key;
    @BindView(R.id.value)
    AppCompatEditText value;
    @BindView(R.id.ok)
    AppCompatButton ok;
    @BindView(R.id.cancel)
    AppCompatButton cancel;


    public static void Edit(Context context,String title,String key,String value,EditResult result){
        EditTextDialog editTextDialog = new EditTextDialog(context,result);
        editTextDialog.t = title;
        editTextDialog.v = value;
        editTextDialog.k = key;
        editTextDialog.show();
    }

    public EditTextDialog(Context context,EditResult result) {
        super(context);
        this.result = result;
    }

    public EditTextDialog(Context context, int theme,EditResult result) {
        super(context, theme);
        this.result = result;
    }

    protected EditTextDialog(Context context, boolean cancelable, OnCancelListener cancelListener,EditResult result) {
        super(context, cancelable, cancelListener);
        this.result = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edittext);
        ButterKnife.bind(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTextDialog.this.dismiss();
                if(result != null)
                    result.onCancel();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditTextDialog.this.dismiss();
                if(result != null)
                    result.onOK(value.getText().toString());
            }
        });

        title.setText(t);
        key.setText(k);
        value.append(v);

    }

    public interface EditResult {

        void onOK(String value);

        void onCancel();

    }
}
