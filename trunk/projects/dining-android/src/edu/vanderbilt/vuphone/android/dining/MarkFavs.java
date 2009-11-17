package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Displays list of restaurants and lets user select new favorites
 * 
 * @author Peter
 */
public class MarkFavs extends ListActivity {

	/** Indicates what dialog is displayed when showDialog is called */
	private static final int DIALOG_ITEM_DISPLAY_CHECKED = 1;
	/** List of the position in list of the restaurants that are selected */
	private ArrayList<Integer> clickedPositions;

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		Bundle extras = getIntent().getExtras();
		int[] favorites = extras.getIntArray("favorites");

		// This section creates the buttons at the bottom of the list.
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(MarkFavs.LAYOUT_INFLATER_SERVICE);
		View footer = inflater.inflate(R.layout.mark_favs_footer, null);
		getListView().addFooterView(footer);
		((Button) findViewById(R.id.Done)).setOnClickListener(listener);
		((Button) findViewById(R.id.Cancel)).setOnClickListener(listener);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				Main.RESTAURANTS));
		getListView().setTextFilterEnabled(true);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		for (int x = 0; x < favorites.length; ++x) {
			getListView().setItemChecked(favorites[x], true);
		}

	}

	/** generates an array of the positions that are clicked */
	private void getClickedPositions() {
		SparseBooleanArray allPositions = getListView()
				.getCheckedItemPositions();
		clickedPositions = new ArrayList<Integer>();
		for (int x = 0; x < Main.RESTAURANTS.length; ++x) {
			if (allPositions.get(x)) {
				clickedPositions.add(x);
			}
		}
		showDialog(DIALOG_ITEM_DISPLAY_CHECKED);
	}

	/** ends activity when button is clicked */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == findViewById(R.id.Done)) {
				getClickedPositions();
			}
			finish();
		}
	};

	/** Creates the dialogs used in the MarkFavs view */
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("The favorites are " + clickedPositions.toString());
		dialog = builder.create();
		return dialog;
	}

	/**
	 * Displays the checked locations and finishes the activity
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			getClickedPositions();
		}
		return false;
	}
}
