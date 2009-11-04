package edu.vanderbilt.vuphone.android.dining;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Displays list of restaurants and lets user select new favorites
 * 
 * @author Peter
 */
public class MarkFavs extends ListActivity {

	/**Indicates what dialog is displayed when showDialog is called*/
	private static final int DIALOG_ITEM_START = 0, DIALOG_ITEM_DISPLAY_CHECKED = 1;
	/**List of the position in list of the restaurants that are selected*/
	private ArrayList<Integer> clickedPositions;

	@Override
	public void onCreate(Bundle ice) {
		super.onCreate(ice);

		showDialog(DIALOG_ITEM_START);
		
		Bundle extras = getIntent().getExtras();
		int[] favorites = extras.getIntArray("favorites");

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				Main.RESTAURANTS));
		getListView().setTextFilterEnabled(true);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		for( int x = 0; x < favorites.length; ++x){
			getListView().setItemChecked(favorites[x],true);
		}

	}

	/** Makes menu button function as a done button */
	public boolean onCreateOptionsMenu(Menu menu) {
		getClickedPositions();
		return true;
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

	/** Creates the dialogs used in the MarkFavs view */
	protected Dialog onCreateDialog(int id) {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {

		case DIALOG_ITEM_DISPLAY_CHECKED:
			builder.setMessage("the items selected are " + clickedPositions);
			break;

		case DIALOG_ITEM_START:
			builder.setMessage("Press menu key when done making selections");
			break;

		default:
			return null;
		}
		dialog = builder.create();
		return dialog;
	}
}
