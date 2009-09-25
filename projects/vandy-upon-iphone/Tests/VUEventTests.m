//
//  VUEventTestCase.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
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
	NSLog(@"setUp");
}

/* The tearDown method is called automatically after each test-case method (methods whose name starts with 'test').
 */
- (void)tearDown {
	NSLog(@"tearDown");
}

- (void)testPass {
	STAssertTrue(true, @"Pass!");
}

- (void)testEventsFromServer
{
	NSManagedObjectContext *context = [[EventStore sharedEventStore] sharedContext];
	NSLog(@"Context: %@", context);
	NSArray *events = [RemoteEventLoader eventsFromServerWithContext:context];
	STAssertNotNil(events, @"events not nil");
	STAssertEquals([events count], 5, @"Doesn't have 5 events");
	NSLog(@"events: %@", events);
}

@end
