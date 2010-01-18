//
//  SearchViewController.m
//  Dining
//
//  Created by Aaron Thompson on 1/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "SearchViewController.h"
#import "RestaurantListViewController.h"
#import "Restaurant.h"
#import "DiningAppDelegate.h"
#import "NSManagedObjectContext-Convenience.h"

@implementation SearchViewController

@synthesize pickerView;

- (void)viewDidLoad
{
	if (self != nil) {
		DiningAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
		context = [appDelegate managedObjectContext];
	}
}


- (NSArray *)restaurants
{
	if (_restaurants == nil) {
		_restaurants = [[context fetchObjectsForEntityName:ENTITY_NAME_RESTAURANT
											 withPredicate:nil] retain];
	}
	
	return _restaurants;
}

- (NSArray *)restaurantTypes
{
	if (_restaurantTypes == nil) {
		NSMutableSet *typesSet = [[NSMutableSet alloc] init];
		
		for (Restaurant *restaurant in [self restaurants])
		{
			[typesSet addObject:restaurant.type];
		}
		
		_restaurantTypes = [[[typesSet allObjects]
							 sortedArrayUsingSelector:@selector(caseInsensitiveCompare:)] retain];
		
		[typesSet release];
		
		// Alloc our array of restaurant selections
		checkedRestaurantTypes = malloc(sizeof(BOOL) * [_restaurantTypes count]);
		for (int i=0; i<[_restaurantTypes count]; i++) {
			checkedRestaurantTypes[i] = NO;
		}
	}
		
	return _restaurantTypes;	
}

#pragma mark UIPickerView

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
	return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
	return [[self restaurantTypes] count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
	return [[self restaurantTypes] objectAtIndex:row];
}

- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view
{
	RestaurantTypePickerView *returnView = nil;
	
	// Reuse the label if possible, otherwise create and configure a new one.
	if (view.tag == RestaurantTypePickerViewTag) {
		returnView = (RestaurantTypePickerView *)view;
	} else {
		NSLog(@"initializing pickerView");
		self.pickerView = [[RestaurantTypePickerView alloc]
						   initWithFrame:CGRectMake(0.0f, 0.0f, 288.0f, 32.0f)
						   buttonTag:row
						   buttonTarget:self
						   buttonAction:@selector(didSelectType:)];
		returnView = self.pickerView;
//		returnView = [[RestaurantTypePickerView alloc]
//					  initWithFrame:CGRectMake(0.0f, 0.0f, 288.0f, 32.0f)
//					  buttonTag:row
//					  buttonTarget:self
//					  buttonAction:@selector(didSelectType:)];
	}
	
	// Where to set the text in depends on what sort of view it is.
	[returnView.nameButton setTitle:[[self restaurantTypes] objectAtIndex:row]];
	returnView.isChecked = checkedRestaurantTypes[row];

	return returnView;
}

- (void)didSelectType:(id)sender
{
	NSInteger row = [sender tag];
	// Toggle the selection at this row
	checkedRestaurantTypes[row] = !checkedRestaurantTypes[row];
	NSLog(@"checked row %i", row);
	[typePicker reloadAllComponents];
}

#pragma mark Searching

- (IBAction)openThruSliderChanged:(id)sender
{
	int hour = (int)[sender value];
	BOOL am = (hour < 12) || (hour == 24);
	if (hour > 12) {
		hour -= 12;
	}
	
	openThruLabel.text = [NSString stringWithFormat:@"%i %@",
						  hour, (am) ? @"am" : @"pm"];
}

- (IBAction)showSearchResults:(id)sender
{
	// Build the type predicate
	NSMutableArray *selectedTypes = [[NSMutableArray alloc] init];
	for (int i=0; i<[[self restaurantTypes] count]; i++) {
		if (checkedRestaurantTypes[i] == YES) {
			[selectedTypes addObject:[_restaurantTypes objectAtIndex:i]];
		}
	}
	
	NSPredicate *typePred = [NSPredicate predicateWithFormat:@"type IN %@", selectedTypes];
	
	// Build the closing time predicate
	NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
	NSDateComponents *dateComponents = [gregorian components:(NSHourCalendarUnit | NSMinuteCalendarUnit)
													fromDate:[NSDate date]];
	NSInteger currentHour = [dateComponents hour];
	NSInteger currentMinute = [dateComponents minute];
	NSInteger latestAllowableMinute = (NSInteger)openThruSlider.value * 60;
	NSInteger minutesUntilClose = latestAllowableMinute - (currentHour * 60 + currentMinute);

	NSPredicate *timePred = [NSPredicate predicateWithFormat:@"minutesUntilClose > %i", minutesUntilClose];
	[gregorian release];
	
	// Build the meal plan predicate
	NSPredicate *mealPlanPred = nil;
	switch (mealPlanChooser.selectedSegmentIndex) {
		case 0:	// Yes
			mealPlanPred = [NSPredicate predicateWithFormat:@"acceptsMealPlan = YES"];
			break;
		case 1:	// No
			mealPlanPred = [NSPredicate predicateWithFormat:@"acceptsMealPlan = NO"];
			break;
		default: // Either
			mealPlanPred = [NSPredicate predicateWithFormat:@"TRUEPREDICATE"];
			break;
	}
	
	NSPredicate *predicate = [NSCompoundPredicate andPredicateWithSubpredicates:
							  [NSArray arrayWithObjects:typePred, timePred, mealPlanPred, nil]];
	
	// Perform the search
	NSArray *filteredRestaurants = [[[self restaurants] filteredArrayUsingPredicate:predicate] mutableCopy];
	
	RestaurantListViewController *listVC = [[RestaurantListViewController alloc]
											initWithNibName:@"RestaurantListViewController" bundle:nil];
	listVC.title = @"Search Results";
	listVC.restaurants = [filteredRestaurants mutableCopy];

	[self.navigationController pushViewController:listVC animated:YES];

	[listVC release];
	[filteredRestaurants release];
	[selectedTypes release];
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
	[super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc
{
	[_restaurants release];
	self.pickerView = nil;

	if (checkedRestaurantTypes) {
		free(checkedRestaurantTypes);
	}
	[super dealloc];
}


@end
