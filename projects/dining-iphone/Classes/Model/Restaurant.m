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
#import "NSString-Regex.h"

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
@dynamic websiteLocationNumber;

static NSDateFormatter *dateFormatter = nil;

+ (void)initialize {
	if (dateFormatter == nil) {
		dateFormatter = [[NSDateFormatter alloc] init];
	}
}

- (NSComparisonResult)compare:(Restaurant *)restaurant
{
	return [self.minutesUntilClose compare:restaurant.minutesUntilClose];
}


- (NSNumber *)minutesUntilClose
{
	[self willAccessValueForKey:@"minutesUntilClose"];
	
	if (_minutesUntilClose)
	{
		NSDate *now = [[NSDate alloc] init];
		NSNumber *result = nil;
		
		[dateFormatter setDateFormat:@"EEEE"];
		NSString *weekDay =  [dateFormatter stringFromDate:now];
		
		// Find the current HourRange
		NSSet *results = [[self openHours] filteredSetUsingPredicate:
						  [NSPredicate predicateWithFormat:@"day = %@", weekDay]];
		
		NSCalendar *calendar = [NSCalendar currentCalendar];
		NSDateComponents *components = [calendar components:NSHourCalendarUnit|NSMinuteCalendarUnit fromDate:now];
		NSInteger hour = [components hour];
		NSInteger minute = [components minute];
		NSInteger minuteOfDayNow = hour * 60 + minute;
		
		// Find the right hour range
		HourRange *range = nil;
		for (HourRange *aRange in results) {
			if (minuteOfDayNow >= [aRange.openMinute intValue] && minuteOfDayNow < aRange.contiguousCloseMinute) {
				range = aRange;
			}
		}
		
		if (range == nil) {
			result = [NSNumber numberWithInt:0];
		} else {
			result = [NSNumber numberWithInt:range.contiguousCloseMinute - minuteOfDayNow];
		}
		
		_minutesUntilClose = [result retain];
		[now release];
	}

	[self didAccessValueForKey:@"minutesUntilClose"];
	
	return _minutesUntilClose;
}


- (NSNumber *)isClosed
{
	[self willAccessValueForKey:@"isClosed"];
	BOOL result = [[self minutesUntilClose] intValue] <= 0;
	[self didAccessValueForKey:@"isClosed"];

	return [NSNumber numberWithBool:result];
}

- (NSArray *)groupedOpenHours
{
	if (_groupedOpenHours == nil) {
		NSSortDescriptor *descriptor = [[NSSortDescriptor alloc] initWithKey:@"order" ascending:YES];
		NSArray *descriptors = [NSArray arrayWithObject:descriptor];
		[descriptor release];
		NSArray *sortedHours = [[self.openHours allObjects] sortedArrayUsingDescriptors:descriptors];
		NSMutableArray *groupedHours = [NSMutableArray new];
		
		for (int i=0; i<[sortedHours count]; i++) {
			HourRange *range = [sortedHours objectAtIndex:i];
			if ([range.openMinute intValue] == 0 && [range.closeMinute intValue] == 1440) {
				// It's open 24-hours, so just add it and continue
				[groupedHours addObject:range];
			} else if (range.contiguousWith != nil) {
				// It has another hour to the right
				[groupedHours addObject:range];
				// Skip ahead by one if the next isn't also 24-hour
				if (!([range.contiguousWith.openMinute intValue] == 0 && [range.contiguousWith.closeMinute intValue] == 1440)) {
					i++;
				}
			} else {
				[groupedHours addObject:range];
			}
			NSLog(@"%@", groupedHours);
		}
		// Handle edge case with Saturday wrapping through Sunday
		if ([groupedHours lastObject] == [groupedHours objectAtIndex:0]) {
			[groupedHours removeObjectAtIndex:[groupedHours count]-1];
		}

		_groupedOpenHours = groupedHours;
	}
	
	return _groupedOpenHours;
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

- (NSArray *)menuItems;
{
	if (!self.websiteLocationNumber) {
		return nil;
	}

	if (!_menuItems)
	{
		NSAutoreleasePool *localPool = [[NSAutoreleasePool alloc] init];
		_menuItems = [[NSMutableArray alloc] init];
		
		// Get current month, day, and year numbers
		NSCalendar *calendar = [NSCalendar currentCalendar];
		NSDateComponents *components = [calendar components:NSMonthCalendarUnit|NSDayCalendarUnit|NSYearCalendarUnit
												   fromDate:[NSDate date]];
		NSInteger month = [components month];
		NSInteger day = [components day];
		NSInteger year = [components year];

		NSString *urlString = [NSString stringWithFormat:@"http://vanderbilt.mymenumanager.net/menu.php?date=%i,%i,%i&location=%@",
							   month, day, year, self.websiteLocationNumber];
		NSData *menuData = [NSData dataWithContentsOfURL:[NSURL URLWithString:urlString]];

		if ([menuData length] > 0)
		{
			NSString *menuString = [[NSString alloc] initWithData:menuData encoding:NSUTF8StringEncoding];

			NSRange ulStartRange = [menuString rangeOfString:@"<ul>"];
			NSRange ulEndRange = [menuString rangeOfString:@"</ul>"];
			NSRange ulRange; // Not including the ul tags themselves
			ulRange.location = ulStartRange.location + 4;	// [@"<ul>" length] == 4
			ulRange.length = ulEndRange.location - ulStartRange.location;
			NSString *stringOfLis = [menuString substringWithRange:ulRange];
			
			[menuString release];

			// Add the string inside each li tag to the array
			NSArray *lis = [stringOfLis componentsSeparatedByString:@"</li>"];	// This will include the starting <li> tag
			for (NSString *li in lis) {
				NSRange liRange = [li rangeOfCharacterFromSet:[NSCharacterSet alphanumericCharacterSet]];

				// Don't add empty ones
				if ((int)[li length] - (int)(liRange.location+3) > 0) {
					NSString *imageFreeString = [li substringFromIndex:liRange.location+3];
					imageFreeString = [imageFreeString stripMedia];
					[(NSMutableArray *)_menuItems addObject:imageFreeString];
				}
			}
		}
		
		[localPool release];
	}
	
	return _menuItems;
}

- (void)dealloc
{
	[_image release];
	[_groupedOpenHours release];
	[_menuItems release];
	[super dealloc];
}


#pragma mark Distance functions

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

#pragma mark MKAnnotation

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
