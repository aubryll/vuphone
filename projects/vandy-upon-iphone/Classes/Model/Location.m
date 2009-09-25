// 
//  Location.m
//  VandyUpon
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

	return locations;
}

@dynamic longitude;
@dynamic name;
@dynamic latitude;
@dynamic events;
@dynamic childLocations;
@dynamic parentLocation;

@end
