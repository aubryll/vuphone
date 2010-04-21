//
//  EventsAppDelegate.h
//  Events
//
//  Created by Aaron Thompson on 9/6/09.
//  Copyright Vanderbilt University 2009. All rights reserved.
//

#import "EventListViewController.h"
#import "MapViewController.h"
#import "AboutViewController.h"

#define DefaultsLastUpdateKey @"lastUpdate"

@interface EventsAppDelegate : NSObject <UIApplicationDelegate> {

    UIWindow *window;
	IBOutlet UITabBarController *tabBarC;
	IBOutlet UINavigationController *eventListNC;
	IBOutlet EventListViewController *eventListVC;
	IBOutlet MapViewController *mapVC;
	UIViewController *loadingViewController;
	
	BOOL aboutViewShowing;
	AboutViewController *aboutViewController;
	IBOutlet UIBarButtonItem *aboutButton;
}

- (IBAction)killApp;
- (IBAction)saveAction:(id)sender;
- (void)getEventsSinceLastUpdate:(NSManagedObjectContext *)context;
- (void)showLoadingView;
- (IBAction)toggleAboutView:(id)sender;
- (void)hidePrompt:(id)sender;

@property (nonatomic, retain) IBOutlet UIWindow *window;

@end
