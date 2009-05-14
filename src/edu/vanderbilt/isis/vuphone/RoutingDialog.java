package edu.vanderbilt.isis.vuphone;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.vanderbilt.isis.R;

/**
 * A dialog class that displays widgets capable of collecting routing information. 
 * @author	Krzysztof Zienkiewicz
 *
 */

public class RoutingDialog extends Dialog{
	
	private RoutingSettings setting_ = null;
	
	public RoutingDialog(Context context, RoutingSettings s){
		super(context);
		super.setTitle("Routing Configuration");
		super.setCancelable(false);
		super.setContentView(R.layout.routing);
		
		setting_ = s;
		this.setupButtons();
	}
	
	/**
	 * Sets up the buttons by assigning them listeners.
	 */
	private void setupButtons(){
		final Button ok = (Button) super.findViewById(R.id.routing_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Update the settings reference and dismiss the dialog
            	RoutingDialog.this.updateSettings();
            	RoutingDialog.this.dismiss();
            }
        });

		final Button cancel = (Button) super.findViewById(R.id.routing_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Cancel the dialog without updating the settings.
            	RoutingDialog.this.cancel();
            }
        });

		final Button clear = (Button) super.findViewById(R.id.routing_clear);
        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Reset the editable fields without saving them. 
            	RoutingDialog.this.clear();
            }
        });
        
	}
	
	/**
	 * Clears the editable fields.
	 */
	private void clear(){
		((EditText) super.findViewById(R.id.string_a_edit)).setText("");
		((EditText) super.findViewById(R.id.int_a_edit)).setText("0");
	}
	
	/**
	 * Saves the information from the editable fields into the reference settings object.
	 */
	private void updateSettings(){
		EditText strEdit = (EditText) super.findViewById(R.id.string_a_edit);
		EditText intEdit = (EditText) super.findViewById(R.id.int_a_edit);
		
		setting_.setStringA(strEdit.getText().toString());
		String numStr = intEdit.getText().toString();
		if (numStr == "")
			setting_.setIntA(0);
		else
			setting_.setIntA(Integer.parseInt(numStr));
	}
}
