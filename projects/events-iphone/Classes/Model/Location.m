// 
//  Location.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "Location.h"
#import "Event.h"
#import "EntityConstants.h"
#import "NSManagedObjectContext-Convenience.h"

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

+ (Location *)locationWithServerId:(NSString *)anId inContext:(NSManagedObjectContext *)context {
	// If the id is nil or empty, don't return a location, because any the user creates will have
	// a null serverId and it shouldn't be overwritten upon an update with bad data
	if (anId == nil || [anId length] == 0) {
		return nil;
	}

	NSSet *locations = [context
						fetchObjectsForEntityName:VUEntityNameLocation
						withPredicateString:@"serverId = %@", anId];
	
	return [locations anyObject];
}

- (BOOL)isEditableByDeviceWithId:(NSString *)deviceId {
	return [self.ownerDeviceId isEqualToString:deviceId];
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

@dynamic ownerDeviceId;
@dynamic serverId;
@dynamic longitude;
@dynamic name;
@dynamic latitude;
@dynamic events;
@dynamic childLocations;
@dynamic parentLocation;

@end
