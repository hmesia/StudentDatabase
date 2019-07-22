package edu.csc4360.studentdatabase;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

//https://developer.android.com/training/search/search

public class StudentDatabase extends Activity implements OnClickListener
{
    //ControllerClassName controllerObject;
    EditText StudentID,Name,Grade;
    Button Add,Delete,Update,Find,ShowAll;
    SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //controllerObject = new ControllerClassName();
        //add paraemters to the constructor of controllerObject, if needed
        StudentID=(EditText)findViewById(R.id.Rollno);
        Name=(EditText)findViewById(R.id.Name);
        Grade=(EditText)findViewById(R.id.Marks);
        Add=(Button)findViewById(R.id.Insert);
        Delete=(Button)findViewById(R.id.Delete);
        Update=(Button)findViewById(R.id.Update);
        Find=(Button)findViewById(R.id.View);
        ShowAll=(Button)findViewById(R.id.ViewAll);

        Add.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Update.setOnClickListener(this);
        Find.setOnClickListener(this);
        ShowAll.setOnClickListener(this);

        // Creating database and table
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }
    public void onClick(View view)
    {
        // Inserting a record to the Student table
        if(view==Add)
        {
            // Checking for empty fields
            if(StudentID.getText().toString().trim().length()==0||
                    Name.getText().toString().trim().length()==0||
                    Grade.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO student VALUES('"+StudentID.getText()+"','"+Name.getText()+
                    "','"+Grade.getText()+"');");
            showMessage("Success", "Record added");
            clearText();
        }
        // Deleting a record from the Student table
        if(view==Delete)
        {
            // Checking for empty roll number
            if(StudentID.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter StudentID");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+StudentID.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM student WHERE rollno='"+StudentID.getText()+"'");
                showMessage("Success", "Record Deleted");
            }
            else
            {
                showMessage("Error", "Invalid StudentID");
            }
            clearText();
        }
        // Updating a record in the Student table
        if(view==Update)
        {
            // Checking for empty roll number
            if(StudentID.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter StudentID");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+StudentID.getText()+"'", null);
            if(c.moveToFirst()) {
                db.execSQL("UPDATE student SET name='" + Name.getText() + "',marks='" + Grade.getText() +
                        "' WHERE rollno='"+StudentID.getText()+"'");
                showMessage("Success", "Record Modified");
            }
            else {
                showMessage("Error", "Invalid StudentID");
            }
            clearText();
        }
        // Display a record from the Student table
        if(view==Find)
        {
            // Checking for empty roll number
            if(StudentID.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter StudentID");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+StudentID.getText()+"'", null);
            if(c.moveToFirst())
            {
                Name.setText(c.getString(1));
                Grade.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid StudentID");
                clearText();
            }
        }
        // Displaying all the records
        if(view==ShowAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("StudentID: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Grade: "+c.getString(2)+"\n\n");
            }
            showMessage("Student Details", buffer.toString());
            //controllerObject.showMessage("Student Details", buffer.toString());
        }
    }
    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        StudentID.setText("");
        Name.setText("");
        Grade.setText("");
        StudentID.requestFocus();
    }
}