//
//  MinutesToCloseSortHelper.m
//  Dining
//
//  Created by Aaron Thompson on 1/16/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "MinutesToCloseSortHelper.h"


@implementation MinutesToCloseSortHelper

@synthesize restaurants;

- (id)initWithRestaurants:(NSArray *)restaurantsArray
{
	self = [super init];
	if (self != nil) {
		self.restaurants = restaurantsArray;
	}
	
	return self;
}

- (NSArray *)sortDescriptors {
	if (_sortDescriptors == nil) {
		// Initialize the array of sort descriptors
		NSSortDescriptor *closedDescriptor = [[NSSortDescriptor alloc] initWithKey:@"isClosed" ascending:YES];
		NSSortDescriptor *minutesToCloseDescriptor = [[NSSortDescriptor alloc] initWithKey:@"minutesUntilClose" ascending:YES];
		_sortDescriptors = [[NSArray alloc] initWithObjects:closedDescriptor, minutesToCloseDescriptor, nil];
		
		[closedDescriptor release];
		[minutesToCloseDescriptor release];
	}

	return _sortDescriptors;
}

// Returns a sorted array of Restaurants grouped by type
- (NSArray *)sections
{
	if (_sections == nil) {
		NSMutableArray *openGroup = [[NSMutableArray alloc] init];
		NSMutableArray *closedGroup = [[NSMutableArray alloc] init];

		for (Restaurant *restaurant in self.restaurants)
		{
			if ([restaurant.minutesUntilClose intValue] > 0) {
				[openGroup addObject:restaurant];
			} else {
				[closedGroup addObject:restaurant];
			}
		}
		
		_sections = [[NSArray alloc] initWithObjects:openGroup, closedGroup, nil];
		
		[openGroup release];
		[closedGroup release];
	}
	
	return _sections;
}

- (NSInteger)numberOfSections {
	return 2;
}

- (NSInteger)numberOfRowsInSection:(NSInteger)section
{
	return [[[self sections] objectAtIndex:section] count];
}

- (NSString *)titleForHeaderInSection:(NSInteger)section
{
	if (section == 0) {
		return @"Open";
	} else {
		return @"Closed";
	}
}

- (void)configureCell:(RestaurantListCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	Restaurant *restaurant = [self restaurantAtIndexPath:indexPath];

	cell.textLabel.text = restaurant.name;
	cell.detailTextLabel.text = restaurant.type;
	
	[cell setMinutesUntilClose:[restaurant.minutesUntilClose integerValue]];
}

- (Restaurant *)restaurantAtIndexPath:(NSIndexPath *)indexPath
{
	return (Restaurant *)[[[self sections] objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
}

- (void)dealloc
{
	[_sortDescriptors release];
	self.restaurants = nil;
	
	[super dealloc];
}

@end
