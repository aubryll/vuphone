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
#import "NameSortHelper.h"
#import "TypeSortHelper.h"
#import "MinutesToCloseSortHelper.h"
#import "DistanceSortHelper.h"

@implementation RestaurantListViewController

@synthesize restaurants;

- (void)viewDidLoad
{
    [super viewDidLoad];
	
	context = [[(DiningAppDelegate *)[[UIApplication sharedApplication] delegate] managedObjectContext] retain];

	if (self.restaurants == nil) {
		self.restaurants = [[[context fetchObjectsForEntityName:ENTITY_NAME_RESTAURANT
											withPredicateString:nil] allObjects] mutableCopy];
	}

	[self initSortHelpers];

	[self setSort:SortIndexName];
}

- (void)initSortHelpers
{
	NameSortHelper *nameHelper = [[NameSortHelper alloc] initWithRestaurants:restaurants];
	
	TypeSortHelper *typeHelper = [[TypeSortHelper alloc] initWithRestaurants:restaurants];
	
	MinutesToCloseSortHelper *minsHelper = [[MinutesToCloseSortHelper alloc] initWithRestaurants:restaurants];

	DistanceSortHelper *distanceHelper = [[DistanceSortHelper alloc] initWithRestaurants:restaurants];
	
	sortHelpers = [[NSArray alloc] initWithObjects:nameHelper, typeHelper, minsHelper, distanceHelper, nil];
	
	// Release all the allocated helpers since the sortHelpers array retained them
	[nameHelper release];
	[typeHelper release];
	[minsHelper release];
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


- (void)tableView:(UITableView *)aTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	RestaurantViewController *rvc = [[RestaurantViewController alloc] initWithNibName:@"RestaurantViewController" bundle:nil];
	rvc.restaurant = (Restaurant *)[[self currentSortHelper] restaurantAtIndexPath:indexPath];
	[self.navigationController pushViewController:rvc animated:YES];
	[rvc release];
	[aTableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (IBAction)sortChanged:(id)sender
{
	[self setSort:((UISegmentedControl *)sender).selectedSegmentIndex];
	
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
