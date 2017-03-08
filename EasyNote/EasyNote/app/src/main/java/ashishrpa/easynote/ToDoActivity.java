package ashishrpa.easynote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ToDoActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et_title, et_description, et_dueDate;
    NumberPicker npMonth;
    NumberPicker npDay;
    NumberPicker npYear;
    int mDay,mMonth,mYear;
    long milliseconds_dueDate;
    private ListView listView;
    private DatabaseAccessToDo databaseAccessToDo;
    private List<ToDo> toDos;
    final String[] monthValues= {"JAN","FEB", "MAR", "APR", "MAY","JUN","JULY", "AUG", "SEP", "OCT","NOV","DEC"};
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy 'at' hh:mm aaa");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        databaseAccessToDo = DatabaseAccessToDo.getInstance(this);

//        npMonth = (NumberPicker) findViewById(R.id.np_month);
//        npDay = (NumberPicker) findViewById(R.id.np_day);
//        npYear = (NumberPicker) findViewById(R.id.np_year);
//
//        //Specify the minimum/maximum value/number of NumberPicker
//        npMonth.setMinValue(0);
//        npMonth.setMaxValue(monthValues.length-1);
//        npMonth.setDisplayedValues(monthValues);
//        npMonth.setWrapSelectorWheel(true);
//
//        npDay.setMinValue(1);
//        npDay.setMaxValue(31);
//        npDay.setWrapSelectorWheel(true);
////        if(mMonth==0||mMonth==2||mMonth==4||mMonth==6||mMonth==7||mMonth==9||mMonth==11){
////            npDay.setMaxValue(31);
////        }else if(mMonth==3||mMonth==5||mMonth==8||mMonth==10){
////            npDay.setMaxValue(30);
////        }else if(mMonth==1||mMonth%4==0){
////            npDay.setMaxValue(29);
////        }else {
////            npDay.setMaxValue(28);
////        }
//
//        npYear.setMinValue(2016);
//        npYear.setMaxValue(2020);
//        npYear.setWrapSelectorWheel(true);
//
//
//        //Set a value change listener for NumberPicker
//        npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
//                //Display the newly selected value from picker
//                //tv.setText("Selected value : " + values[newVal]);
//                mMonth = newVal;
//            }
//        });
//
//        npDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
//                mDay = newVal;
//            }
//        });
//
//        npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
//                mYear = newVal;
//            }
//        });


        listView = (ListView) findViewById(R.id.listView_for_todo);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDo toDo = toDos.get(position);
                TextView txtDescription = (TextView) view.findViewById(R.id.tv_todo_description);
                if (toDo.isFullDisplayed()) {
                    txtDescription.setText(toDo.getShortDescription());
                    toDo.setFullDisplayed(false);
                } else {
                    txtDescription.setText(toDo.getDescription());
                    toDo.setFullDisplayed(true);
                }
            }
        });
    }
    @Override
    protected void onRestart() {

        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(ToDoActivity.this, ToDoActivity.class);  //your class
        startActivity(i);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addtodo: {
                Intent intentToDo = new Intent(this, ToDoEditActivity.class);
                Log.e("TDA", "ToDoEdit");
                startActivity(intentToDo);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        databaseAccessToDo.open();
        this.toDos = databaseAccessToDo.getAllToDos();
        databaseAccessToDo.close();
        ToDoAdapter adapter = new ToDoAdapter(this, toDos);
        this.listView.setAdapter(adapter);
    }

    public void onAddTodo(ToDo toDo) {
        DatabaseAccessToDo databaseAccessToDo = DatabaseAccessToDo.getInstance(this);
        databaseAccessToDo.open();
        databaseAccessToDo.saveToDo(toDo);
        databaseAccessToDo.close();
        //this.finish();
        onRestart();
        Log.e("TDA : ","ToDo Saved");

    }

    public void onDeleteToDoClicked(ToDo toDo) {
        databaseAccessToDo.open();
        databaseAccessToDo.deleteToDo(toDo);
        databaseAccessToDo.close();

        ArrayAdapter<ToDo> adapter = (ArrayAdapter<ToDo>) listView.getAdapter();
        adapter.remove(toDo);
        adapter.notifyDataSetChanged();
    }

    public void onEditToDoClicked(ToDo toDo) {
        Intent intent = new Intent(this, ToDoEditActivity.class);
        intent.putExtra("TODO", toDo);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }


    private class ToDoAdapter extends ArrayAdapter<ToDo> {


        public ToDoAdapter(Context context, List<ToDo> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.layout_todo_list_item, parent, false);
            }

            ImageView btnEdit = (ImageView) convertView.findViewById(R.id.todo_btnEdit);
            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.todo_btnDelete);
            TextView textViewHeading = (TextView) convertView.findViewById(R.id.tv_todo_heading);
            TextView textViewTitle = (TextView) convertView.findViewById(R.id.tv_todo_title);
            TextView textViewDescription = (TextView) convertView.findViewById(R.id.tv_todo_description);
            TextView textViewDueDate = (TextView) convertView.findViewById(R.id.tv_todo_duedate);

            final ToDo toDo = toDos.get(position);
            toDo.setFullDisplayed(false);
            textViewHeading.setText(toDo.getDate());
            textViewTitle.setText(toDo.getTitle());
            textViewDescription.setText(toDo.getDescription());
            textViewDueDate.setText(toDo.getToDoDate());
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditToDoClicked(toDo);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteToDoClicked(toDo);
                }
            });
            return convertView;
        }
    }
//    public void addToDoDialog(){
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ToDoActivity.this);
//
//        // Get the layout inflater
//        LayoutInflater inflater = this.getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        alertDialog.setView(inflater.inflate(R.layout.add_dialog, null))
//                // Add action buttons
//
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                })
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        Dialog diag = (Dialog) dialog;
//                        et_title = (EditText) diag.findViewById(R.id.et_add_diag_title);
//                        et_description = (EditText) diag.findViewById(R.id.et_add_diag_description);
//                        et_dueDate = (EditText) diag.findViewById(R.id.et_add_diag_duedate);
//                        String title = et_title.getText().toString();
//                        String description = et_description.getText().toString();
//
//                        // Start
//                        npMonth = (NumberPicker) diag.findViewById(R.id.np_month);
//                        npDay = (NumberPicker) diag.findViewById(R.id.np_day);
//                        npYear = (NumberPicker) diag.findViewById(R.id.np_year);
//
//                        //Specify the minimum/maximum value/number of NumberPicker
//                        npMonth.setMinValue(0);
//                        //npMonth.setMaxValue(monthValues.length-1);
//                        npMonth.setMaxValue(12);
//                       // npMonth.setDisplayedValues(monthValues);
//                        npMonth.setWrapSelectorWheel(true);
//
//                        npDay.setMinValue(1);
//                        npDay.setMaxValue(31);
//                        npDay.setWrapSelectorWheel(true);
//
//                        npYear.setMinValue(2016);
//                        npYear.setMaxValue(2020);
//                        npYear.setWrapSelectorWheel(true);
//
//
//                        //Set a value change listener for NumberPicker
//                        npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                            @Override
//                            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
//                                //Display the newly selected value from picker
//                                //tv.setText("Selected value : " + values[newVal]);
//                                mMonth = npMonth.getValue();
//                            }
//                        });
//
//                        npDay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                            @Override
//                            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
//                                mDay = npDay.getValue();
//                            }
//                        });
//
//                        npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//                            @Override
//                            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
//                                mYear = npYear.getValue();
//                            }
//                        });
//
//                        //End
//                        long duedate = prepareDateInLong();
//                        long currentDateTime = new Date().getTime();
//                        if(((title.length()>4) && !title.equals(null)) && ((description.length()>4) && !description.equals(null))){
//                            ToDo toDo = new ToDo(currentDateTime,title,description,currentDateTime,0);  //DUEDATE NOT UPDATED
//                            onAddTodo(toDo);
//                        }else if((title.length()<5)||title.equals(null)){
//                            Toast.makeText(getApplicationContext(),"Title atleast 5 char",Toast.LENGTH_SHORT).show();
//                        }else if((description.length()<5)||description.equals(null)){
//                            Toast.makeText(getApplicationContext(),"Description atleast 5 char",Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });
//        alertDialog.show();
//    }

    public long prepareDateInLong(){
        String string_date =mDay+"-"+mMonth+"-"+mYear;
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date d = f.parse(string_date);
            milliseconds_dueDate = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds_dueDate;
    }
}
