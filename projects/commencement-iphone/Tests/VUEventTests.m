//
//  VUEventTestCase.m
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <SenTestingKit/SenTestingKit.h>
#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "RemoteEventLoader.h"
#import "EventStore.h"

@interface VUEventTests : SenTestCase {
}

@end

@implementation VUEventTests

/* The setUp method is called automatically before each test-case method (methods whose name starts with 'test').
 */
- (void)setUp {
}

/* The tearDown method is called automatically after each test-case method (methods whose name starts with 'test').
 */
- (void)tearDown {
}

- (void)testEventsFromServer
{
	NSManagedObjectContext *context = [[EventStore sharedEventStore] sharedContext];
	NSLog(@"Context: %@", context);
	NSArray *events = [RemoteEventLoader eventsFromServerWithContext:context];
	STAssertNotNil(events, @"events not nil");
	NSLog(@"Events: %@", events);
	STAssertEquals([events count], 2, @"Doesn't have 2 events");
}

@end
