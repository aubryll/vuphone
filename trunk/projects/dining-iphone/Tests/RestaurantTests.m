//
//  RestaurantTests.m
//  Dining
//
//  Created by Aaron Thompson on 1/14/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>
#import "RestaurantTests.h"
#import "DiningAppDelegate.h"
#import "Restaurant.h"
#import "HourRange.h"

@implementation RestaurantTests

- (void)setUp {
	NSLog(@"setUp");
  
	managedObjectModel = [[NSManagedObjectModel mergedModelFromBundles:nil] retain];

	persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:managedObjectModel];
	NSError *error;
	if (![persistentStoreCoordinator addPersistentStoreWithType:NSInMemoryStoreType configuration:nil URL:nil options:nil error:&error]) {
		STFail(@"Failed to initialize persistent store coordinator");
	}
	
	context = [[NSManagedObjectContext alloc] init];
	[context setPersistentStoreCoordinator:persistentStoreCoordinator];
}

- (void)testMinutesUntilClose
{
	// Add a couple Restaurants
	Restaurant *earlyCloser = (Restaurant *)[NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_RESTAURANT
																		  inManagedObjectContext:context];
	earlyCloser.name = @"Closes early";
	
	HourRange *range1 = (HourRange *)[NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_HOUR_RANGE
																   inManagedObjectContext:context];
	range1.day = @"Thursday";
	range1.openMinute = [NSNumber numberWithInt:200];
	range1.closeMinute = [NSNumber numberWithInt:1200];
	[earlyCloser addOpenHoursObject:range1];
	
	
	Restaurant *lateCloser = (Restaurant *)[NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_RESTAURANT
																		 inManagedObjectContext:context];
	lateCloser.name = @"Closes late";
	
	HourRange *range2 = (HourRange *)[NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_HOUR_RANGE
																   inManagedObjectContext:context];
	range2.day = @"Thursday";
	range2.openMinute = [NSNumber numberWithInt:800];
	range2.closeMinute = [NSNumber numberWithInt:1438];
	[lateCloser addOpenHoursObject:range2];

	NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"minutesUntilClose" ascending:YES];

	STAssertTrue([sortDescriptor compareObject:earlyCloser toObject:lateCloser] == NSOrderedAscending,
				 @"Early restaurant before late is ordered ascending");
	STAssertTrue([sortDescriptor compareObject:lateCloser toObject:earlyCloser] == NSOrderedDescending,
				 @"Late restaurant before early is ordered descending");

	[sortDescriptor release];
}

- (void)tearDown {
	[context release];
	[managedObjectModel release];
	[persistentStoreCoordinator release];
}


@end
