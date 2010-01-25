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
	DiningAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	context = [appDelegate managedObjectContext];

	// Alloc our array of restaurant selections
	checkedRestaurantTypes = malloc(sizeof(BOOL) * [[self restaurantTypes] count]);
	for (int i=0; i<[[self restaurantTypes] count]; i++) {
		checkedRestaurantTypes[i] = YES;
	}
	
	[tableView reloadData];
}

- (void)viewWillAppear:(BOOL)animated
{
	self.parentViewController.navigationItem.leftBarButtonItem = checkboxButton;
}

- (void)viewWillDisappear:(BOOL)animated
{
	self.parentViewController.navigationItem.leftBarButtonItem = nil;
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
	}
		
	return _restaurantTypes;	
}

#pragma mark UITableViewDataSource, Delegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)aTableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)aTableView numberOfRowsInSection:(NSInteger)section {
    return [[self restaurantTypes] count];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{    
	static NSString *CellIdentifier = @"Cell";
	
	UITableViewCell *cell = [aTableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
	}
	
	cell.textLabel.text = [[self restaurantTypes] objectAtIndex:indexPath.row];
	
	if (checkedRestaurantTypes[indexPath.row] == YES) {
		cell.accessoryType = UITableViewCellAccessoryCheckmark;
	} else {
		cell.accessoryType = UITableViewCellAccessoryNone;
	}
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 32.0f;
}

- (void)tableView:(UITableView *)aTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	// Flip the flag
	checkedRestaurantTypes[indexPath.row] = !checkedRestaurantTypes[indexPath.row];

	// Deselect this row and refresh it
	[aTableView deselectRowAtIndexPath:indexPath animated:YES];
	[aTableView reloadRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
}

#pragma mark Searching

- (IBAction)toggleCheckAll:(id)sender
{
	// First, check to see if they are all checked
	int checkedCount = 0;
	for (int i=0; i<[[self restaurantTypes] count]; i++)
	{
		if (checkedRestaurantTypes[i]) {
			checkedCount++;
		}
	}
	BOOL allChecked = checkedCount == [[self restaurantTypes] count];
	
	if (allChecked)
	{
		// Deselect all
		for (int i=0; i<[[self restaurantTypes] count]; i++) {
			checkedRestaurantTypes[i] = NO;
		}
	}
	else
	{
		// Select all
		for (int i=0; i<[[self restaurantTypes] count]; i++) {
			checkedRestaurantTypes[i] = YES;
		}
	}
	
	[tableView reloadData];
}

- (IBAction)openThruSliderChanged:(UISlider *)sender
{
	int hour = (int)sender.value;
	NSString *labelText = nil;
	
	if (hour < 1) {
		labelText = @"anytime";
	} else if (hour == 24) {
		labelText = @"midnight";
	} else {
		BOOL am = (hour < 12) || (hour == 24);
		if (hour > 12) {
			hour -= 12;
		}
		
		labelText = [NSString stringWithFormat:@"%i %@", hour, (am) ? @"am" : @"pm"];
	}

	openThruLabel.text = labelText;
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
	NSPredicate *timePred = nil;
	if (openThruSlider.value < 1.0f) {
		timePred = [NSPredicate predicateWithFormat:@"TRUEPREDICATE"];
	} else {
		NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
		NSDateComponents *dateComponents = [gregorian components:(NSHourCalendarUnit | NSMinuteCalendarUnit)
														fromDate:[NSDate date]];
		NSInteger currentHour = [dateComponents hour];
		NSInteger currentMinute = [dateComponents minute];
		NSInteger latestAllowableMinute = (NSInteger)openThruSlider.value * 60;
		NSInteger minutesUntilClose = latestAllowableMinute - (currentHour * 60 + currentMinute);

		timePred = [NSPredicate predicateWithFormat:@"minutesUntilClose > %i", minutesUntilClose];
		[gregorian release];
	}
	
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
	
	// Build the on-/off-campus predicate
	NSPredicate *onCampusPred = nil;
	switch (onCampusChooser.selectedSegmentIndex) {
		case 0:	// Yes
			onCampusPred = [NSPredicate predicateWithFormat:@"offCampus = NO"];
			break;
		case 1:	// No
			onCampusPred = [NSPredicate predicateWithFormat:@"offCampus = YES"];
			break;
		default: // Either
			onCampusPred = [NSPredicate predicateWithFormat:@"TRUEPREDICATE"];
			break;
	}
	
	NSPredicate *predicate = [NSCompoundPredicate andPredicateWithSubpredicates:
							  [NSArray arrayWithObjects:typePred, timePred, mealPlanPred, onCampusPred, nil]];
	
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
