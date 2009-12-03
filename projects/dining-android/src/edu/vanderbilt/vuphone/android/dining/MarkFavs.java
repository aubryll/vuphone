package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
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
	public static final String EXTRA_FAVORITES = "favorites";
	/**
	 * The position in list Main.RESTAURANTS of the restaurants that are
	 * currently marked as favorites
	 */
	private ArrayList<Integer> clickedPositions;

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		setContentView(R.layout.mark_favs);

		Bundle extras = getIntent().getExtras();
		int[] favorites = extras.getIntArray(EXTRA_FAVORITES);

		// This section creates the buttons at the bottom of the list.
		((Button) findViewById(R.mark_favs.done)).setOnClickListener(listener);
		((Button) findViewById(R.mark_favs.cancel))
				.setOnClickListener(listener);

//		setListAdapter(new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_multiple_choice,
//				Main.RESTAURANTS));
		getListView().setTextFilterEnabled(true);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		if (favorites.length != 0) {
			for (int x = 0; x < favorites.length; ++x) {
				getListView().setItemChecked(favorites[x], true);
			}
		}

	}

	/** generates an array of the positions that are clicked */
	private void getClickedPositions() {
		SparseBooleanArray allPositions = getListView()
				.getCheckedItemPositions();
		clickedPositions = new ArrayList<Integer>();
//		for (int x = 0; x < Main.RESTAURANTS.length; ++x) {
//			if (allPositions.get(x)) {
//				clickedPositions.add(x);
//			}
//		}
		showDialog(DIALOG_ITEM_DISPLAY_CHECKED);
	}

	/** ends activity when button is clicked */
	private OnClickListener listener = new OnClickListener() {

		public void onClick(View v) {
			if (v.equals(findViewById(R.mark_favs.done))) {
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
