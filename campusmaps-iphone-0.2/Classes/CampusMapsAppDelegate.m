//
//  CampusMapsAppDelegate.m
//  CampusMaps
//
//  Created by Aaron Thompson on 10/6/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "CampusMapsAppDelegate.h"
#import "RemotePOILoader.h"
#import "Layer.h"
#import "LocationManagerSingleton.h"
#import <CoreLocation/CoreLocation.h>

@implementation CampusMapsAppDelegate

@synthesize window;

#define DatabaseFilename @"CampusMaps.sqlite"
#define DefaultDatabaseFilename @"CampusMapsDefaultData"
#define DefaultDatabaseFilenameExtension @"sqlite"
#define DefaultsLastUpdateKey @"DefaultsLastUpdateKey"

#pragma mark -
#pragma mark Application lifecycle

- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    
	// This call should set up our singleton. It returns a reference to our singleton
	// but we don't need it right here.
	[LocationManagerSingleton sharedManager];
	
	mapViewController.managedObjectContext = [self managedObjectContext];
	
	// Add a layer if none exist
	NSSet *allLayers = [Layer allLayers:[self managedObjectContext]];
	if ([allLayers count] == 0) {
		Layer *layer = [NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_LAYER
													 inManagedObjectContext:[self managedObjectContext]];
		layer.name = @"Buildings";
		NSError *err = nil;
		[[self managedObjectContext] save:&err];
		if (err) {
			NSLog(@"Error upon adding a default POI: %@", err);
		}
	}
	
	
	[window addSubview:navController.view];
	[navController pushViewController:mapViewController animated:NO];
	[window makeKeyAndVisible];
/*	
	// Update if it hasn't within 24 hours
	NSDate *lastUpdate = [[NSUserDefaults standardUserDefaults] objectForKey:DefaultsLastUpdateKey];
	if (lastUpdate == nil) {
		lastUpdate = [NSDate dateWithTimeIntervalSince1970:0];
	}

	if ([[NSDate date] timeIntervalSinceDate:lastUpdate] > 60*60*24) {
		[self performSelectorInBackground:@selector(loadRemotePOIs:) withObject:[self managedObjectContext]];
	}
*/
}

- (void)loadRemotePOIs:(NSManagedObjectContext *)context
{
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	
	[RemotePOILoader getPOIsFromServerIntoContext:context];

	NSError *err = nil;
	[context save:&err];
	if (err) {
		NSLog(@"Error upon loading POIs: %@", err);
	}
	
	// Set the current date as last updated
	[[NSUserDefaults standardUserDefaults] setObject:[NSDate date] forKey:DefaultsLastUpdateKey];
	
	[pool release];
}

/**
 applicationWillTerminate: saves changes in the application's managed object context before the application terminates.
 */
- (void)applicationWillTerminate:(UIApplication *)application {
	
    NSError *error = nil;
    if (managedObjectContext != nil) {
        if ([managedObjectContext hasChanges] && ![managedObjectContext save:&error]) {
			/*
			 Replace this implementation with code to handle the error appropriately.
			 
			 abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development. If it is not possible to recover from the error, display an alert panel that instructs the user to quit the application by pressing the Home button.
			 */
			NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
			abort();
        } 
    }
}


#pragma mark -
#pragma mark Core Data stack

/**
 Returns the managed object context for the application.
 If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
 */
- (NSManagedObjectContext *)managedObjectContext {
	
    if (managedObjectContext != nil) {
        return managedObjectContext;
    }
	
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (coordinator != nil) {
        managedObjectContext = [[NSManagedObjectContext alloc] init];
        [managedObjectContext setPersistentStoreCoordinator: coordinator];
    }
    return managedObjectContext;
}


/**
 Returns the managed object model for the application.
 If the model doesn't already exist, it is created by merging all of the models found in the application bundle.
 */
- (NSManagedObjectModel *)managedObjectModel {
	
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
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator {
	
    if (persistentStoreCoordinator != nil) {
        return persistentStoreCoordinator;
    }
	
	NSString *storePath = [[self applicationDocumentsDirectory] stringByAppendingPathComponent:DatabaseFilename];
	
	// Copy the default database if none exists
	if (![[NSFileManager defaultManager] fileExistsAtPath:storePath])
	{
		NSString *defaultStorePath = [[NSBundle mainBundle] pathForResource:DefaultDatabaseFilename ofType:DefaultDatabaseFilenameExtension];
		NSError *err = nil;
		[[NSFileManager defaultManager] copyItemAtPath:defaultStorePath
												toPath:storePath
												 error:&err];
		if (err) {
			NSLog(@"Error copying default store file at %@ to %@:\n%@", defaultStorePath, storePath, err);
		}
	}

    NSURL *storeUrl = [NSURL fileURLWithPath:storePath];

	// Set options to automatically perform a lightweight migration
	NSDictionary *options = [NSDictionary dictionaryWithObjectsAndKeys:
							 [NSNumber numberWithBool:YES], NSMigratePersistentStoresAutomaticallyOption,
							 [NSNumber numberWithBool:YES], NSInferMappingModelAutomaticallyOption, nil];
	NSError *error = nil;
    persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    if (![persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeUrl options:options error:&error]) {
		/*
		 Replace this implementation with code to handle the error appropriately.
		 
		 abort() causes the application to generate a crash log and terminate.
		 You should not use this function in a shipping application, although it may be useful during development.
		 If it is not possible to recover from the error, display an alert panel that instructs the user
		 to quit the application by pressing the Home button.
		 
		 Typical reasons for an error here include:
		 * The persistent store is not accessible
		 * The schema for the persistent store is incompatible with current managed object model
		 Check the error message to determine what the actual problem was.
		 */
		NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
		abort();
    }    
	
    return persistentStoreCoordinator;
}


#pragma mark -
#pragma mark Application's Documents directory

/**
 Returns the path to the application's Documents directory.
 */
- (NSString *)applicationDocumentsDirectory {
	return [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) lastObject];
}


#pragma mark -
#pragma mark Memory management

- (void)dealloc {
	
    [managedObjectContext release];
    [managedObjectModel release];
    [persistentStoreCoordinator release];
    
	[window release];
	[super dealloc];
}


@end

