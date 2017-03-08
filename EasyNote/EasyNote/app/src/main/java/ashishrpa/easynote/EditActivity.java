package ashishrpa.easynote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etText;
    private Memo memo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etText = (EditText) findViewById(R.id.etText);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            memo = (Memo) bundle.get("MEMO");
            if (memo != null) {
                this.etText.setText(memo.getText());
            }
        }
    }


    public void onSaveClicked() {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        if(memo == null) {
            // Add new memo
            Memo temp = new Memo();
            temp.setText(etText.getText().toString());
            databaseAccess.saveMemo(temp);
        } else {
            // Update the memo
            memo.setText(etText.getText().toString());
            databaseAccess.updateMemo(memo);
        }
        databaseAccess.close();
        this.finish();
    }

    public void onCancelClicked() {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                onSaveClicked();
                Log.e("EA : ","Save Clicked");
                break;
            case R.id.btnCancel:
                onCancelClicked();
                Log.e("EA : ","Cancel Clicked");
                break;
            default:
                break;
        }
    }
}
