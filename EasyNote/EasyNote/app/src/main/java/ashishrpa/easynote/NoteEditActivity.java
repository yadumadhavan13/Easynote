package ashishrpa.easynote;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteEditActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText noteEditText;
    private Memo note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        noteEditText = (EditText) findViewById(R.id.noteEdit_etText);
        Button btnNoteSave = (Button) findViewById(R.id.noteEdit_btnSave);
        Button btnNoteCancel = (Button) findViewById(R.id.noteEdit_btnCancel);
        btnNoteSave.setOnClickListener(this);
        btnNoteCancel.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            note = (Memo) bundle.get("NOTE");
            if (note != null) {
                this.noteEditText.setText(note.getText());
            }
        }
    }

    public void onSaveClicked() {
        DatabaseAccessNotes databaseAccessNotes = DatabaseAccessNotes.getInstance(this);
        databaseAccessNotes.open();
        if(note == null) {
            // Add new note
            Memo temp = new Memo();
            temp.setText(noteEditText.getText().toString());
            databaseAccessNotes.saveNotes(temp);
        } else {
            // Update the note
            note.setText(noteEditText.getText().toString());
            databaseAccessNotes.updateNotes(note);
        }
        databaseAccessNotes.close();
        this.finish();
    }

    public void onCancelClicked() {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noteEdit_btnSave:
                onSaveClicked();
                Log.e("NEA : ","Save Clicked");
                break;
            case R.id.noteEdit_btnCancel:
                onCancelClicked();
                Log.e("NEA : ","Cancel Clicked");
                break;
            default:
                break;
        }
    }
}
