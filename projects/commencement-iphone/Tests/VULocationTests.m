//
//  VULocationTestCase.m
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <SenTestingKit/SenTestingKit.h>
#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "EventStore.h"
#import "Location.h"

@interface VULocationTests : SenTestCase {
}

@end

@implementation VULocationTests

/* The setUp method is called automatically before each test-case method (methods whose name starts with 'test').
 */
- (void)setUp {
}

/* The tearDown method is called automatically after each test-case method (methods whose name starts with 'test').
 */
- (void)tearDown {
}

- (void)testRootLocations
{
	NSManagedObjectContext *context = [[EventStore sharedEventStore] sharedContext];
	NSLog(@"Context: %@", context);
	NSArray *locations = [Location rootLocations:context];
	STAssertNotNil(locations, @"Locations not nil");
	NSLog(@"Locations: %@", locations);
	STAssertEquals([locations count], 2, @"Doesn't have 2 locations");
}

@end
