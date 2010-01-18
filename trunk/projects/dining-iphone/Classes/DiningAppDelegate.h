//
//  DiningAppDelegate.h
//  Dining
//
//  Created by Aaron Thompson on 1/10/10.
//  Copyright __MyCompanyName__ 2010. All rights reserved.
//

#import "Restaurant.h"

@interface DiningAppDelegate : NSObject <UIApplicationDelegate> {

    NSManagedObjectModel *managedObjectModel;
    NSManagedObjectContext *managedObjectContext;	    
    NSPersistentStoreCoordinator *persistentStoreCoordinator;

    UIWindow *window;
	IBOutlet UINavigationController *navController;
	IBOutlet UITabBarController *tabBarController;
}

@property (nonatomic, retain, readonly) NSManagedObjectModel *managedObjectModel;
@property (nonatomic, retain, readonly) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, retain, readonly) NSPersistentStoreCoordinator *persistentStoreCoordinator;

@property (nonatomic, retain) IBOutlet UIWindow *window;

- (void)showRestaurantOnMap:(Restaurant *)restaurant;

- (NSString *)applicationDocumentsDirectory;

@end

