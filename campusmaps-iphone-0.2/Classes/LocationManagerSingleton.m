//
//  LocationManagerSingleton.m
//  CampusMaps
//
//  Created by Demetri Miller on 12/25/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LocationManagerSingleton.h"


static LocationManagerSingleton *sharedLocationManagerSingleton = nil;

@implementation LocationManagerSingleton

@synthesize locationManager;

#pragma mark -
#pragma mark Location Management

// Handles setup of the locationManager. Includes assigning delegate 
// and updating location.
- (void)setupLocationManager
{
	if (locationManager.locationServicesEnabled == NO) {
		NSLog(@"Location services not enabled.");
	}
	
	locationManager.delegate = self;
	locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters;
	locationManager.distanceFilter = 10.0f; // In meters.
	[locationManager startUpdatingLocation];
}

// Returns the most recent location known to the locationManager.
- (CLLocation *)lastKnownLocation
{
	// Location corresponds to the center of campus, Nashville, TN. This is hard-coded
	// for now because accessing location-based services on simulator is not working.
	CLLocation *loc = [[[CLLocation alloc] initWithLatitude:36.142 longitude:-86.8044] autorelease];
	return loc;
	
}

#pragma mark -
#pragma mark Delegate Methods
/**
 *	For now, these methods do nothing.
 */
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation
fromLocation:(CLLocation *)oldLocation
{
	NSLog(@"Received new location");
}

- (void)locationmanager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
	NSLog(@"Error: %@", [error localizedDescription]);
}



#pragma mark -
#pragma mark Singleton Methods
+ (LocationManagerSingleton *)sharedManager
{
	if (sharedLocationManagerSingleton == nil) {
		sharedLocationManagerSingleton = [[super allocWithZone:NULL] init];
		[sharedLocationManagerSingleton setupLocationManager];
	}
	return sharedLocationManagerSingleton;
}

+ (id)allocWithZone:(NSZone *)zone
{
	return [[self sharedManager] retain];
}

- (id)copyWithZone:(NSZone *)zone
{
	return self;
}

- (id)retain
{
	return self;
}

- (NSUInteger)retainCount
{
	return NSUIntegerMax; // Denotes an object that cannot be released.
}

- (void)released
{
	// Do nothing.
}

- (id)autorelease
{
	return self;
}


@end
