//
//  RestaurantListViewController.m
//  Dining
//
//  Created by Aaron Thompson on 1/11/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantListViewController.h"
#import "RestaurantListCell.h"
#import "RestaurantViewController.h"
#import "NSManagedObjectContext-Convenience.h"
#import "DiningAppDelegate.h"
#import "Restaurant.h"
#import "RestaurantListSortHelper.h"
#import "MinutesToCloseSortHelper.h"
#import "TypeSortHelper.h"
#import "DistanceSortHelper.h"

@implementation RestaurantListViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	
	context = [[(DiningAppDelegate *)[[UIApplication sharedApplication] delegate] managedObjectContext] retain];

	restaurants = [[[context fetchObjectsForEntityName:ENTITY_NAME_RESTAURANT
								   withPredicateString:nil] allObjects] mutableCopy];
	[self initSortHelpers];

	 [self setSort:SortIndexMinutesUntilClose];
}

- (void)initSortHelpers {
	MinutesToCloseSortHelper *minsHelper = [[MinutesToCloseSortHelper alloc] initWithRestaurants:restaurants];

	TypeSortHelper *typeHelper = [[TypeSortHelper alloc] initWithRestaurants:restaurants];

	DistanceSortHelper *distanceHelper = [[DistanceSortHelper alloc] initWithRestaurants:restaurants];
	
	sortHelpers = [[NSArray alloc] initWithObjects:minsHelper, typeHelper, distanceHelper, nil];
	
	// Release all the allocated helpers since the sortHelpers array retained them
	[minsHelper release];
	[typeHelper release];
	[distanceHelper release];
}

- (id<RestaurantListSortHelper>)currentSortHelper
{
	return [sortHelpers objectAtIndex:currentSortHelperIndex];
}

- (void)viewDidUnload {
	[sortHelpers release];
	[restaurants release];
	[context release];

    [super viewDidUnload];
}


- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}


#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return [[self currentSortHelper] numberOfSections];
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [[self currentSortHelper] numberOfRowsInSection:section];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
	return [[self currentSortHelper] titleForHeaderInSection:section];
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{    
	static NSString *CellIdentifier = @"Cell";

	RestaurantListCell *cell = (RestaurantListCell *)[aTableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[RestaurantListCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] autorelease];
	}
	
	[[self currentSortHelper] configureCell:cell atIndexPath:indexPath];

	return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	RestaurantViewController *rvc = [[RestaurantViewController alloc] initWithNibName:@"RestaurantViewController" bundle:nil];
	rvc.restaurant = (Restaurant *)[[self currentSortHelper] restaurantAtIndexPath:indexPath];
	[self.navigationController pushViewController:rvc animated:YES];
	[rvc release];
}

- (IBAction)sortChanged:(id)sender
{
	switch (((UISegmentedControl *)sender).selectedSegmentIndex) {
		case 0:	// Time to close
			[self setSort:SortIndexMinutesUntilClose];
			break;
		case 1:	// Type
			[self setSort:SortIndexType];
			break;
		case 2:	// Distance
			[self setSort:SortIndexDistance];
			break;
		default:
			break;
	}
	
	// Scroll to the top
	[tableView setContentOffset:CGPointMake(0, 0) animated:NO];
}

- (void)setSort:(SortHelperIndex)index
{
	currentSortHelperIndex = index;

	id<RestaurantListSortHelper> sortHelper = [sortHelpers objectAtIndex:index];
	
	[restaurants sortUsingDescriptors:[sortHelper sortDescriptors]];
	
	[tableView reloadData];
}

- (void)dealloc {
    [super dealloc];
}


@end
