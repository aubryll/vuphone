package org.vuphone.wwatch.android;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class Tabs extends TabActivity {
	public void onCreate(Bundle ice) {
		super.onCreate(ice);
		
		TabHost th = getTabHost();
		th.addTab(th.newTabSpec("Tab1").setIndicator("Settings").setContent(new Intent(this, SettingsUI.class)));
		th.addTab(th.newTabSpec("Tab2").setIndicator("Testing").setContent(new Intent(this, TestingUI.class)));
		th.addTab(th.newTabSpec("Tab3").setIndicator("Wrecks").setContent(new Intent(this, AccidentViewer.class)));
		th.addTab(th.newTabSpec("Tab4").setIndicator("Contacts").setContent(new Intent(this, ContactPicker.class)));
		
		th.setCurrentTab(2);
	}
}
