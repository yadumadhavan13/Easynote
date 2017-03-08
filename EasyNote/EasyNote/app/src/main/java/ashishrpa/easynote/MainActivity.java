package ashishrpa.easynote;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private DatabaseAccess databaseAccess;
    private List<Memo> memos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseAccess = DatabaseAccess.getInstance(this);

        listView = (ListView) findViewById(R.id.listView);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo memo = memos.get(position);
                TextView txtMemo = (TextView) view.findViewById(R.id.txtMemo);
                if (memo.isFullDisplayed()) {
                    txtMemo.setText(memo.getShortText());
                    memo.setFullDisplayed(false);
                } else {
                    txtMemo.setText(memo.getText());
                    memo.setFullDisplayed(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseAccess.open();
        this.memos = databaseAccess.getAllMemos();
        databaseAccess.close();
        MemoAdapter adapter = new MemoAdapter(this, memos);
        this.listView.setAdapter(adapter);
    }

    public void onAddClicked() {
        Intent intent = new Intent(this, EditActivity.class);
        Log.e("MA : ","AddButton Clicked");
        startActivity(intent);
    }

    public void onDeleteClicked(Memo memo) {
        databaseAccess.open();
        databaseAccess.deleteMemo(memo);
        databaseAccess.close();

        ArrayAdapter<Memo> adapter = (ArrayAdapter<Memo>) listView.getAdapter();
        adapter.remove(memo);
        adapter.notifyDataSetChanged();
    }

    public void onEditClicked(Memo memo) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("MEMO", memo);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                onAddClicked();
                break;
            default:
                break;
        }
    }

    private class MemoAdapter extends ArrayAdapter<Memo> {


        public MemoAdapter(Context context, List<Memo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_list_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.btnEdit);
            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.btnDelete);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            TextView txtMemo = (TextView) convertView.findViewById(R.id.txtMemo);

            final Memo memo = memos.get(position);
            memo.setFullDisplayed(false);
            txtDate.setText(memo.getDate());
            txtMemo.setText(memo.getShortText());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClicked(memo);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteClicked(memo);
                }
            });
            return convertView;
        }
    }
}