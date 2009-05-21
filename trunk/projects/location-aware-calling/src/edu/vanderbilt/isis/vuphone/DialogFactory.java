package edu.vanderbilt.isis.vuphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.vanderbilt.isis.R;

/**
 * A convenience class that consolidates dialog creation.
 * 
 * @author Krzysztof Zienkiewcz
 *
 */
// TODO - Refactor this class to make it more efficient and readable

public class DialogFactory{

	/**
	 * Private constructor to disable instantiation.
	 */
	private DialogFactory(){}
	
	public static Dialog createQuitDialog(final Activity context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Are you sure you want to exit?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
		           public void onClick(DialogInterface dialog, int id){
		                context.finish();
		           }
		       })
		       .setNegativeButton("No", null);
						/*new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });*/
		return builder.create();
	}
	
	public static Dialog createRoutingDialog(Activity context){
	
		View view = context.getLayoutInflater().inflate(R.layout.routing, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Edit configurations")
			.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int which){
					Dialog dialog = (Dialog) d;
					// Fetch current settings
					
					Zone zone = ZoneManager.getInstance().getEditing();
					String name = zone.getName();
					RoutingSettings settings = ZoneManager.getInstance().getSettings(name);
					if (settings == null){
						// Create the record because it doesn't exist;
						settings = new RoutingSettings();
					}
					
					// Save the data.
					settings.setIntA(Integer.parseInt(((EditText) dialog.findViewById(R.id.int_a_edit)).getText().toString()));
					settings.setStringA(((EditText) dialog.findViewById(R.id.string_a_edit)).getText().toString());
					ZoneManager.getInstance().setSettings(name, settings);					
				}
			})
			.setNegativeButton("Cancel", null)
			.setView(view);
		
		Dialog dialog = builder.create();
		
		
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){
			public void onDismiss(DialogInterface dialog){
				ZoneManager.getInstance().setEditing(null);
			}
		});
		
		return dialog;
	}
	
	public static void updateRoutingDialog(Dialog dialog){
		Zone zone = ZoneManager.getInstance().getEditing();
		String name = zone.getName();

		TextView text = (TextView) dialog.findViewById(R.id.zone_name);
		text.setText(name);
		
		RoutingSettings settings = ZoneManager.getInstance().getSettings(name);
		
		if (settings == null){
			// display default data
			((EditText) dialog.findViewById(R.id.string_a_edit)).setText("Default string");
			((EditText) dialog.findViewById(R.id.int_a_edit)).setText("0");
		}else{
			((EditText) dialog.findViewById(R.id.string_a_edit)).setText(settings.getStringA());
			((EditText) dialog.findViewById(R.id.int_a_edit)).setText((new Integer(settings.getIntA())).toString());
		}
	}
}
