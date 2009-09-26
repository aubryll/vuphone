//
//  EventsAppDelegate.h
//  Events
//
//  Created by Aaron Thompson on 9/6/09.
//  Copyright Iostudio, LLC 2009. All rights reserved.
//

#import "EventListViewController.h"
#import "MapViewController.h"

@interface EventsAppDelegate : NSObject <UIApplicationDelegate> {

    UIWindow *window;
	IBOutlet UITabBarController *tabBarC;
	IBOutlet UINavigationController *eventListNC;
	IBOutlet EventListViewController *eventListVC;
	IBOutlet MapViewController *mapVC;
}

- (IBAction)saveAction:(id)sender;

@property (nonatomic, retain) IBOutlet UIWindow *window;

@end
