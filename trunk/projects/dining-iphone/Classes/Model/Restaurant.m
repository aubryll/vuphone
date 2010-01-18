// 
//  Restaurant.m
//  Dining
//
//  Created by Aaron Thompson on 1/10/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Restaurant.h"
#import "HourRange.h"
#import "NSManagedObjectContext-Convenience.h"
#import "LocationManagerSingleton.h"

@implementation Restaurant 

@dynamic acceptsMealPlan;
@dynamic details;
@dynamic imageUrlString;
@dynamic phone;
@dynamic offCampus;
@dynamic longitude;
@dynamic latitude;
@dynamic type;
@dynamic acceptsMealMoney;
@dynamic urlString;
@dynamic name;
@dynamic openHours;
@dynamic minutesUntilClose;
@dynamic isClosed;
@dynamic distanceInFeet;

- (NSComparisonResult)compare:(Restaurant *)restaurant
{
	return [self.minutesUntilClose compare:restaurant.minutesUntilClose];
}


- (NSNumber *)minutesUntilClose
{
	[self willAccessValueForKey:@"minutesUntilClose"];
	
	NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
	[dateFormatter setDateFormat:@"EEEE"];
	NSString *weekDay =  [dateFormatter stringFromDate:[NSDate date]];

	// Find the current HourRange
	NSSet *results = [[self managedObjectContext]
					  fetchObjectsForEntityName:ENTITY_NAME_HOUR_RANGE
							withPredicateString:@"restaurant = %@ AND day = %@",
												self, weekDay];
	HourRange *range = [results anyObject];
	
	if (!range) {
		return 0;
	}
	
	NSDate *now = [NSDate date];
	NSCalendar *calendar = [NSCalendar currentCalendar];
	NSDateComponents *components = [calendar components:NSHourCalendarUnit|NSMinuteCalendarUnit fromDate:now];
	NSInteger hour = [components hour];
	NSInteger minute = [components minute];
	
	NSInteger minuteOfDayNow = hour * 60 + minute;
	
	NSNumber *result = nil;
	if (minuteOfDayNow < [range.openMinute intValue]) {
		// Not yet open
		result = [NSNumber numberWithInt:minuteOfDayNow - [range.openMinute intValue]];
	} else {
		result = [NSNumber numberWithInt:[range.closeMinute intValue] - minuteOfDayNow];
	}
	
	[self didAccessValueForKey:@"minutesUntilClose"];
	
	return result;
}


- (NSNumber *)isClosed
{
	[self willAccessValueForKey:@"isClosed"];
	BOOL result = [[self minutesUntilClose] intValue] <= 0;
	[self didAccessValueForKey:@"isClosed"];

	return [NSNumber numberWithBool:result];
}

- (void)deleteAllOpenHours
{
	for (HourRange *range in self.openHours) {
		[[self managedObjectContext] deleteObject:range];
	}
}

+ (Restaurant *)restaurantWithName:(NSString *)aName inContext:(NSManagedObjectContext *)context {
	NSSet *restaurants = [context fetchObjectsForEntityName:ENTITY_NAME_RESTAURANT
										withPredicateString:@"name = %@", aName];
	
	return [restaurants anyObject];
}

// Returns the image for this POI, whose URL is specified in the url property
- (UIImage *)image 
{
	if (!_image) {
		// Load the image
		NSString *path = [[NSBundle mainBundle] resourcePath];
		path = [path stringByAppendingPathComponent:@"RestaurantIcons"];
		path = [path stringByAppendingPathComponent:self.imageUrlString];

		NSData *imageData = [NSData dataWithContentsOfFile:path];
		if (imageData != nil) {
			_image = [[UIImage alloc] initWithData:imageData];
		}
	}
	
	return _image;
}

- (NSNumber *)distanceInFeet
{
	CLLocation *poiLocation = [[[CLLocation alloc] initWithLatitude:[self.latitude doubleValue] 
														  longitude:[self.longitude doubleValue]] autorelease];

	CLLocation *currentLocation = [[LocationManagerSingleton sharedManager] lastKnownLocation];
	CLLocationDistance distance = [poiLocation getDistanceFrom:currentLocation];

	return [NSNumber numberWithDouble:distance];
}

- (NSString *)distanceAsString
{
	return [Restaurant prettyDistance:[self.distanceInFeet doubleValue]];
}

// Returns the distance to the POI from the location specified...Formatted as a string.
- (NSString *)distanceFromLocation:(CLLocation *)location
{
	CLLocation *poiLocation = [[[CLLocation alloc] initWithLatitude:[self.latitude doubleValue] 
														  longitude:[self.longitude doubleValue]] autorelease];
	// Distance measured in meters. 
	CLLocationDistance distance = [poiLocation getDistanceFrom:location];
	
	return [Restaurant prettyDistance:distance];
}

+ (NSString *)prettyDistance:(CLLocationDistance)distanceInFeet
{
	// Return the result with the proper units appended to the string.
	if (distanceInFeet > 600.0f) {
		return [NSString stringWithFormat:@"%.2f miles", distanceInFeet/5280];
	} else {
		return [NSString stringWithFormat:@"%.f feet", distanceInFeet];
	}	
}

- (CLLocationCoordinate2D)coordinate {
	CLLocationCoordinate2D coord;
	coord.longitude = [self.longitude doubleValue];	
	coord.latitude = [self.latitude doubleValue];
	
	return coord;
}

- (NSString *)title {
	return self.name;
}

- (NSString *)subtitle {
	return self.type;
}

@end
