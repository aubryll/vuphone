//
//  DiningAppDelegate.m
//  Dining
//
//  Created by Aaron Thompson on 1/10/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "DiningAppDelegate.h"
#import "RestaurantListViewController.h"
#import "MapViewController.h"
#import "RestaurantImporterXML.h"

@implementation DiningAppDelegate

@synthesize window;


- (void)showRestaurantOnMap:(Restaurant *)restaurant
{
	RestaurantListViewController *listVC = [[tabBarController viewControllers] objectAtIndex:0];
	[listVC.navigationController popToRootViewControllerAnimated:NO];
	
	[tabBarController setSelectedIndex:2];
	
	MapViewController *mapVC = [[tabBarController viewControllers] objectAtIndex:2];
	[mapVC selectRestaurant:restaurant];
}

#pragma mark -
#pragma mark Application lifecycle

- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    
	[navController pushViewController:tabBarController animated:NO];
	
	[window addSubview:navController.view];

	[window makeKeyAndVisible];
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
	
	NSString *storePath = [[self applicationDocumentsDirectory] stringByAppendingPathComponent:@"Dining.sqlite"];
    NSURL *storeUrl = [NSURL fileURLWithPath:storePath];
	
	BOOL isNew = ! ([[NSFileManager defaultManager] fileExistsAtPath:storePath]);
	
	NSError *error = nil;
    persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel:[self managedObjectModel]];
    if (![persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeUrl options:nil error:&error]) {
		/*
		 Replace this implementation with code to handle the error appropriately.
		 
		 abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development. If it is not possible to recover from the error, display an alert panel that instructs the user to quit the application by pressing the Home button.
		 
		 Typical reasons for an error here include:
		 * The persistent store is not accessible
		 * The schema for the persistent store is incompatible with current managed object model
		 Check the error message to determine what the actual problem was.
		 */
		NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
		abort();
    }
	
	if (isNew) {
		// Load restaurant data from XML using a temporary managed object context
		NSManagedObjectContext *importContext = [[NSManagedObjectContext alloc] init];
		[importContext setPersistentStoreCoordinator:persistentStoreCoordinator];
		[importContext setUndoManager:nil];

		[RestaurantImporterXML getRestaurantsFromXMLAtPath:[[NSBundle mainBundle] pathForResource:@"restaurants" ofType:@"xml"]
											   intoContext:[self managedObjectContext]];
		[importContext release];
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

