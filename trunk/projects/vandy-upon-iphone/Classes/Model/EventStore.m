//
//  EventStore.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/13/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "EventStore.h"


@implementation EventStore

#pragma mark Singleton methods

static EventStore *eventStore = nil;

+ (EventStore *)sharedEventStore
{
	@synchronized(self) {
		if (eventStore == nil) {
			[[self alloc] init];
		}
	}
	
	return eventStore;
}

+ (id)allocWithZone:(NSZone *)zone
{
	@synchronized(self) {
		if (eventStore == nil) {
			eventStore = [super allocWithZone:zone];
			
			return eventStore;	// assignment and return on first allocation
		}
	}
	
	return nil;	// on subsequent allocation attempts, return nil
}

- (id)copyWithZone:(NSZone *)zone
{
	return self;
}

- (unsigned)retainCount
{
	return UINT_MAX;	// denotes that it cannot be released
}

- (void)release
{
	// do nothing
}

- (id)autorelease
{
	return self;
}

- (void)dealloc
{
	[managedObjectContext release];
	[managedObjectModel release];
	[persistentStoreCoordinator release];

	[super dealloc];
}


#pragma mark Class methods

/**
 Returns the managed object context for the application.
 If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
 */
- (NSManagedObjectContext *)sharedContext
{
	if (managedObjectContext != nil) {
		return managedObjectContext;
	}
	
	NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
	if (coordinator != nil) {
		managedObjectContext = [[NSManagedObjectContext alloc] init];
		[managedObjectContext setPersistentStoreCoordinator:coordinator];
	}

	return managedObjectContext;
}


/**
 Returns the managed object model for the application.
 If the model doesn't already exist, it is created by merging all of the models found in the application bundle.
 */
- (NSManagedObjectModel *)managedObjectModel
{
	if (managedObjectModel != nil) {
		return managedObjectModel;
	}
	managedObjectModel = [[NSManagedObjectModel mergedModelFromBundles:nil] retain];

	return managedObjectModel;
}


/**
 Returns the persistent store coordinator for the application.
 If the coordinator doesn't already exist, it is created and the application's store added to it.
 */
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator
{
	if (persistentStoreCoordinator != nil) {
		return persistentStoreCoordinator;
	}
	
	NSURL *storeUrl = [NSURL fileURLWithPath:[[self documentsDirectory] stringByAppendingPathComponent:@"VandyUpon.sqlite"]];
	
	NSError *error;
	persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
	if (![persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeUrl options:nil error:&error]) {
		// Handle error
		NSLog(@"Error loading the persistent store coordinator: %@", error);
	}

	return persistentStoreCoordinator;
}


#pragma mark -
#pragma mark Application's documents directory

/**
 Returns the path to the application's documents directory.
 */
- (NSString *)documentsDirectory {
	
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	NSString *basePath = ([paths count] > 0) ? [paths objectAtIndex:0] : nil;

	return basePath;
}

@end
