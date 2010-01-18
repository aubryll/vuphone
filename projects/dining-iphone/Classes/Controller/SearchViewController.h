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

@interface SearchViewController : UIViewController <UIPickerViewDataSource, UIPickerViewDelegate> {
	IBOutlet UIPickerView *typePicker;
	IBOutlet UISlider *openThruSlider;
	IBOutlet UILabel *openThruLabel;
	IBOutlet UISegmentedControl *mealPlanChooser;
	RestaurantTypePickerView *pickerView;
	
	NSManagedObjectContext *context;
	NSArray *_restaurants;
	NSArray *_restaurantTypes;
	BOOL *checkedRestaurantTypes;
}

@property (retain) RestaurantTypePickerView *pickerView;

- (NSArray *)restaurants;
- (NSArray *)restaurantTypes;
- (void)didSelectType:(id)sender;
- (IBAction)openThruSliderChanged:(id)sender;
- (IBAction)showSearchResults:(id)sender;

@end
