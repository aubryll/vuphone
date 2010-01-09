package edu.vanderbilt.vuphone.android.objects;

import java.util.ArrayList;

public class RestaurantMenu {
	
	private ArrayList<MenuItem> _items;
	

	public static class MenuItem {
		private String _name;
		private String _description;
		// nutrition facts data in the future
		
		public MenuItem() {
			this("", "");
		}
		
		public MenuItem(String name) {
			this(name, "");
		}
		
		public MenuItem(String name, String description) {
			_name = name;
			_description = description;
		}
		
		public String getName() {
			return _name;
		}

		public String getDescription() {
			return _description;
		}
		
		// The mutator methods allows public modify access, maybe shouldnt
		// to be consistent with the rest of Restaurant mutator methods.
		public void setName(String name) {
			_name = name;
		}
		
		public void setDescription(String description) {
			_description = description;
		}
	}
	
	public RestaurantMenu() {
		this(null);
	}
	
	public RestaurantMenu(ArrayList<MenuItem> items) {
		setItems(items);
	}
	
	public boolean hasItems() {
		return !_items.isEmpty();
	}
	
	public ArrayList<MenuItem> getItems() {
		return _items;
	}
	
	public void setItems(ArrayList<MenuItem> items) {
		if (items == null)
			_items = new ArrayList<MenuItem>();
		else _items = items;
	}
	
	public void addItem(MenuItem item) {
		_items.add(item);
	}
}
