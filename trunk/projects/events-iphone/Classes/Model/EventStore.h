//
//  EventStore.h
//  Events
//
//  Created by Aaron Thompson on 9/13/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@interface EventStore : NSObject {

    NSManagedObjectModel *managedObjectModel;
    NSManagedObjectContext *managedObjectContext;	    
    NSPersistentStoreCoordinator *persistentStoreCoordinator;
}

/**
 A singleton method to get the shared event store
 */
+ (EventStore *)sharedEventStore;

/**
 A convenience method which creates an autoreleased editing context with the shared event store
 */
- (NSManagedObjectContext *)editingContext;

/**
 Returns the managed object context for the application.
 If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
 */
- (NSManagedObjectContext *)sharedContext;

/**
 Returns the managed object model for the application.
 If the model doesn't already exist, it is created by merging all of the models found in the application bundle.
 */
- (NSManagedObjectModel *)managedObjectModel;

/**
 Returns the persistent store coordinator for the application.
 If the coordinator doesn't already exist, it is created and the application's store added to it.
 */
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator;

/**
 Returns the path to the application's documents directory.
 */
- (NSString *)documentsDirectory;

@end
