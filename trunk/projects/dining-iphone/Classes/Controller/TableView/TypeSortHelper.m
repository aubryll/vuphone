//
//  KindSortHelper.m
//  Dining
//
//  Created by Aaron Thompson on 1/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "TypeSortHelper.h"


@implementation TypeSortHelper

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
		NSSortDescriptor *kindDescriptor = [[NSSortDescriptor alloc] initWithKey:@"type" ascending:YES];
		_sortDescriptors = [[NSArray alloc] initWithObjects:kindDescriptor, nil];
		
		[kindDescriptor release];
	}
	
	return _sortDescriptors;
}

// Returns a sorted array of Restaurants grouped by type
- (NSArray *)sections
{
	if (_sections == nil) {
		NSMutableDictionary *groups = [[NSMutableDictionary alloc] init];

		for (Restaurant *restaurant in self.restaurants)
		{
			NSMutableArray *group = (NSMutableArray *)[groups objectForKey:restaurant.type];
			if (group == nil) {
				group = [[[NSMutableArray alloc] init] autorelease];
			}

			[group addObject:restaurant];

			[groups setValue:group forKey:restaurant.type];
		}

		NSArray *sortedKeys = [[groups allKeys] sortedArrayUsingSelector:@selector(caseInsensitiveCompare:)];
		_sections = [[groups objectsForKeys:sortedKeys notFoundMarker:@"VUKeyNotFound"] retain];

		[groups release];
	}
	
	return _sections;
}

- (NSInteger)numberOfSections {
	return [[self sections] count];
}

- (NSInteger)numberOfRowsInSection:(NSInteger)section
{
	return [[[self sections] objectAtIndex:section] count];
}

- (NSString *)titleForHeaderInSection:(NSInteger)section
{
	Restaurant *aRestaurant = (Restaurant *)[[[self sections] objectAtIndex:section] objectAtIndex:0];

	return aRestaurant.type;
}


- (void)configureCell:(RestaurantListCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	Restaurant *restaurant = [self restaurantAtIndexPath:indexPath];

	cell.textLabel.text = restaurant.name;
	cell.detailTextLabel.text = [restaurant distanceAsString];
	
	[cell setMinutesUntilClose:restaurant.minutesUntilClose];
}

- (Restaurant *)restaurantAtIndexPath:(NSIndexPath *)indexPath
{
	return (Restaurant *)[[[self sections] objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
}

- (void)dealloc
{
	[_sortDescriptors release];
	[_sections release];
	self.restaurants = nil;

	[super dealloc];
}


@end
