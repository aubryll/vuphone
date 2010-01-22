//
//  DistanceSortHelper.m
//  Dining
//
//  Created by Aaron Thompson on 1/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "DistanceSortHelper.h"


@implementation DistanceSortHelper

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
		NSSortDescriptor *distanceDescriptor = [[NSSortDescriptor alloc] initWithKey:@"distanceInFeet" ascending:YES];
		_sortDescriptors = [[NSArray alloc] initWithObjects:distanceDescriptor, nil];
		
		[distanceDescriptor release];
	}
	
	return _sortDescriptors;
}

- (NSInteger)numberOfSections {
	return 1;
}

- (NSInteger)numberOfRowsInSection:(NSInteger)section
{
	return [restaurants count];
}

- (NSString *)titleForHeaderInSection:(NSInteger)section
{
	return nil;
}


- (void)configureCell:(RestaurantListCell *)cell atIndexPath:(NSIndexPath *)indexPath
{
	Restaurant *restaurant = [self restaurantAtIndexPath:indexPath];
	
	cell.textLabel.text = restaurant.name;
	cell.detailTextLabel.text = [restaurant distanceAsString];
	
	[cell setMinutesUntilClose:[restaurant.minutesUntilClose integerValue]];
}

- (Restaurant *)restaurantAtIndexPath:(NSIndexPath *)indexPath
{
	return (Restaurant *)[self.restaurants objectAtIndex:indexPath.row];
}

- (void)dealloc
{
	[_sortDescriptors release];
	self.restaurants = nil;
	
	[super dealloc];
}


@end
