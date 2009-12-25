//
//  CampusMapsAppDelegate.h
//  CampusMaps
//
//  Created by Aaron Thompson on 10/6/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "MapViewController.h"
#import <CoreLocation/CoreLocation.h>

@interface CampusMapsAppDelegate : NSObject <UIApplicationDelegate, CLLocationManagerDelegate> {

    NSManagedObjectModel *managedObjectModel;
    NSManagedObjectContext *managedObjectContext;	    
    NSPersistentStoreCoordinator *persistentStoreCoordinator;
    UIWindow *window;
	
	//CLLocationManager *locationManager;
	
	IBOutlet MapViewController *mapViewController;
	IBOutlet UINavigationController *navController;
	
}

- (void)loadRemotePOIs:(NSManagedObjectContext *)context;

@property (nonatomic, retain, readonly) NSManagedObjectModel *managedObjectModel;
@property (nonatomic, retain, readonly) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, retain, readonly) NSPersistentStoreCoordinator *persistentStoreCoordinator;

@property (nonatomic, retain) IBOutlet UIWindow *window;

//@property (retain) CLLocationManager *locationManager;

//- (CLLocation *)mostRecentLocation;

- (NSString *)applicationDocumentsDirectory;

@end

