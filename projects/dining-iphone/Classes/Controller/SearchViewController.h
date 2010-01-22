//
//  SearchViewController.h
//  Dining
//
//  Created by Aaron Thompson on 1/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "RestaurantTypePickerView.h"

@interface SearchViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	IBOutlet UITableView *tableView;
	IBOutlet UISlider *openThruSlider;
	IBOutlet UILabel *openThruLabel;
	IBOutlet UISegmentedControl *mealPlanChooser;
	IBOutlet UISegmentedControl *onCampusChooser;
	RestaurantTypePickerView *pickerView;
	
	NSManagedObjectContext *context;
	NSArray *_restaurants;
	NSArray *_restaurantTypes;
	BOOL *checkedRestaurantTypes;
}

@property (retain) RestaurantTypePickerView *pickerView;

- (NSArray *)restaurants;
- (NSArray *)restaurantTypes;
- (IBAction)openThruSliderChanged:(UISlider *)sender;
- (IBAction)showSearchResults:(id)sender;

@end
