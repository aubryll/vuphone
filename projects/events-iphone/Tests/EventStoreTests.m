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

@interface EventStoreTests : SenTestCase {
}

@end

@implementation EventStoreTests

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

- (void)testEventStore
{
	EventStore *store = [EventStore sharedEventStore];
	STAssertNotNil(store, @"Shared event store not nil");
	
	NSPersistentStoreCoordinator *coordinator = [store persistentStoreCoordinator];
	STAssertNotNil(coordinator, @"Persistent store coordinator not nil");
	
	NSManagedObjectModel *model = [store managedObjectModel];
	STAssertNotNil(model, @"Managed object model not nil");
	
	NSManagedObjectContext *context = [[EventStore sharedEventStore] sharedContext];
	STAssertNotNil(context, @"Shared managed object context not nil");

	context = [[EventStore sharedEventStore] editingContext];
	STAssertNotNil(context, @"Managed object context not nil");
}

- (void)replaceManagedObjectModelMethod {
	
}
/*
- (NSManagedObjectModel *)managedObjectModel
{
	if (managedObjectModel != nil) {
		return managedObjectModel;
	}
	managedObjectModel = [[NSManagedObjectModel mergedModelFromBundles:nil] retain];
	
	return managedObjectModel;
}
*/

@end
