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

public class NotesListActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private DatabaseAccessNotes databaseAccessNotes;
    private List<Memo> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        databaseAccessNotes = DatabaseAccessNotes.getInstance(this);

        listView = (ListView) findViewById(R.id.lv_notes);
        Button btnAddNotes = (Button) findViewById(R.id.btnAddNotes);
        btnAddNotes.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Memo note = notes.get(position);
                TextView txtNote = (TextView) view.findViewById(R.id.note_txtNote);
                if (note.isFullDisplayed()) {
                    txtNote.setText(note.getShortText());
                    note.setFullDisplayed(false);
                } else {
                    txtNote.setText(note.getText());
                    note.setFullDisplayed(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseAccessNotes.open();
        this.notes = databaseAccessNotes.getAllNotes();
        databaseAccessNotes.close();
        NotesAdapter adapter = new NotesAdapter(this, notes);
        this.listView.setAdapter(adapter);
    }

    public void onAddNotes() {
        Intent intent = new Intent(this, NoteEditActivity.class);
        Log.e("NLA : ","AddNotesButton Clicked");
        startActivity(intent);
    }

    public void onDeleteNoteClicked(Memo note) {
        databaseAccessNotes.open();
        databaseAccessNotes.deleteNotes(note);
        databaseAccessNotes.close();

        ArrayAdapter<Memo> adapter = (ArrayAdapter<Memo>) listView.getAdapter();
        adapter.remove(note);
        adapter.notifyDataSetChanged();
    }

    public void onEditNoteClicked(Memo note) {
        Intent intent = new Intent(this, NoteEditActivity.class);
        intent.putExtra("NOTE", note);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddNotes:
                onAddNotes();
                break;
            default:
                break;
        }
    }

    private class NotesAdapter extends ArrayAdapter<Memo> {


        public NotesAdapter(Context context, List<Memo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_notes_list_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.note_btnEdit);
            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.note_btnDelete);
            TextView txtDate = (TextView) convertView.findViewById(R.id.note_txtDate);
            TextView txtNote = (TextView) convertView.findViewById(R.id.note_txtNote);

            final Memo note = notes.get(position);
            note.setFullDisplayed(false);
            txtDate.setText(note.getDate());
            txtNote.setText(note.getShortText());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditNoteClicked(note);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteNoteClicked(note);
                }
            });
            return convertView;
        }
    }
}
