//
//  CampusMapsAppDelegate.h
//  CampusMaps
//
//  Created by Aaron Thompson on 10/6/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "MapViewController.h"
#import <CoreLocation/CoreLocation.h>
#import "AboutViewController.h"

@interface CampusMapsAppDelegate : NSObject <UIApplicationDelegate, CLLocationManagerDelegate> {

    NSManagedObjectModel *managedObjectModel;
    NSManagedObjectContext *managedObjectContext;	    
    NSPersistentStoreCoordinator *persistentStoreCoordinator;
    UIWindow *window;
	
	IBOutlet MapViewController *mapViewController;
	IBOutlet UINavigationController *navController;

	BOOL aboutViewShowing;
	AboutViewController *aboutViewController;
}

- (void)loadRemotePOIs:(NSManagedObjectContext *)context;
- (void)toggleAboutView;

@property (nonatomic, retain, readonly) NSManagedObjectModel *managedObjectModel;
@property (nonatomic, retain, readonly) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, retain, readonly) NSPersistentStoreCoordinator *persistentStoreCoordinator;

@property (nonatomic, retain) IBOutlet UIWindow *window;

- (NSString *)applicationDocumentsDirectory;

@end
