package org.vuphone.wwatch.android;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ContactPicker extends Activity implements View.OnClickListener {
    
    private String[] contactNames_;		// All names with non null numbers
    private String[] contactNumbers_;	// All non null numbers
    
    // List to hold the selected numbers
    private final ArrayList<String> numberList_ = new ArrayList<String>();
	
    private ListView listView_;
    private Button submitButton_;
    private Button clearButton_;
    private Button cancelButton_;
    
    public void onClick(View v) {
    	if (v.equals(submitButton_)) {
    		// fill numberList_ with checked contacts
    		SparseBooleanArray choices = listView_.getCheckedItemPositions();
    		for (int i = 0; i < choices.size(); ++i) {
    			int realIndex = choices.keyAt(i);
    			if (choices.get(realIndex))
    				numberList_.add(contactNumbers_[realIndex]);    		
    		}
    		
    	}else if (v.equals(clearButton_)) {
    		for (int i = 0; i < contactNames_.length; ++i)
    			listView_.setItemChecked(i, false);
    		return;
    	}else if (v.equals(cancelButton_)) {
    		
    	}
    	
    	Toast.makeText(this, "numberList_: " + numberList_.toString(), 
    			Toast.LENGTH_LONG).show();
    	super.finish();
    }
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.pickerview);
        
        listView_ = (ListView) super.findViewById(R.id.list_view);
        submitButton_ = (Button) super.findViewById(R.id.submit_button);
        submitButton_.setOnClickListener(this);
        
        clearButton_ = (Button) super.findViewById(R.id.clear_button);
        clearButton_.setOnClickListener(this);
        
        cancelButton_ = (Button) super.findViewById(R.id.cancel_button);
        cancelButton_.setOnClickListener(this);
        
        this.loadNamesAndNumbers();
        
        listView_.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, contactNames_));

        listView_.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView_.setItemsCanFocus(true);
        listView_.setTextFilterEnabled(true);
    }

    private void loadNamesAndNumbers() {
    	Cursor c = super.getContentResolver().query(
    		Contacts.People.CONTENT_URI, null, null, null, Contacts.PeopleColumns.NAME);
    	
    	int totalSize = c.getCount();	// May include null numbers
    	int nameCol = c.getColumnIndex(Contacts.PeopleColumns.NAME);
    	int numberCol = c.getColumnIndex(Contacts.Phones.NUMBER);

    	ArrayList<String> tempNameList = new ArrayList<String>();
    	ArrayList<String> tempNumList = new ArrayList<String>();
    	
    	c.moveToFirst();    	
    	for (int index = 0; index < totalSize; ++index) {
    		String num = c.getString(numberCol);
    		if (num != null) {
    			tempNameList.add(c.getString(nameCol));
    			tempNumList.add(c.getString(numberCol));
    		}
    		c.moveToNext();
    	}
    	c.deactivate();
    	
    	contactNames_ = new String[tempNameList.size()];
    	contactNumbers_ = new String[tempNumList.size()];
    	
    	tempNameList.toArray(contactNames_);
    	tempNumList.toArray(contactNumbers_);
    }
}
