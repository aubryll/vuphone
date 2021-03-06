//
//  Campus_TourAppDelegate.m
//  Campus Tour
//
//  Created by Aaron Thompson on 2/9/10.
//  Copyright Vanderbilt University 2010. All rights reserved.
//

#import "Campus_TourAppDelegate.h"
#import "RootViewController.h"


@implementation Campus_TourAppDelegate

@synthesize window;
@synthesize navigationController;


#pragma mark -
#pragma mark Application lifecycle

- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    
    // Override point for customization after app launch    
	
	[window addSubview:[navigationController view]];
    [window makeKeyAndVisible];
}


- (void)applicationWillTerminate:(UIApplication *)application {
	// Save data if appropriate
}


#pragma mark -
#pragma mark Memory management

- (void)dealloc {
	[navigationController release];
	[window release];
	[super dealloc];
}


@end

