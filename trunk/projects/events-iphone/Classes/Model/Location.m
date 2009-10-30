// 
//  Location.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "Location.h"
#import "Event.h"
#import "EntityConstants.h"

@implementation Location 

+ (NSArray *)rootLocations:(NSManagedObjectContext *)context
{
	// Create fetch request and entity description
	NSFetchRequest *request = [[NSFetchRequest alloc] init];
	[request setEntity:[NSEntityDescription entityForName:VUEntityNameLocation inManagedObjectContext:context]];
	
	// Add predicate
	[request setPredicate:[NSPredicate predicateWithFormat:@"parentLocation == NULL"]];
	
	// Execute request
	NSError *error = nil;
	NSArray *locations = [context executeFetchRequest:request error:&error];

	if (error) {
		NSLog(@"Error upon fetching root locations: %@, %@", error, [error userInfo]);
	}
	
	[request release];

	return locations;
}

#pragma mark MKAnnotation methods

- (NSString *)title {
	return self.name;
}

- (NSString *)subtitle {
	return nil;
}

- (CLLocationCoordinate2D)coordinate {
	CLLocationCoordinate2D coords;
	coords.latitude = [self.latitude doubleValue];
	coords.longitude = [self.longitude doubleValue];
	return coords;
}

@dynamic longitude;
@dynamic name;
@dynamic latitude;
@dynamic events;
@dynamic childLocations;
@dynamic parentLocation;

@end
