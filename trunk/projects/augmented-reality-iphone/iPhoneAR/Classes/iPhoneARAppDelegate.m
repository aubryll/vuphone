//
//  iPhoneARAppDelegate.m
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright Gotow.net Creative 2009. All rights reserved.
//

#import "iPhoneARAppDelegate.h"
#import "ARViewController.h"

@implementation iPhoneARAppDelegate

@synthesize window;
@synthesize viewController;


- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    
    // Override point for customization after app launch    
    [window addSubview:viewController.view];
    [window makeKeyAndVisible];
}


- (void)dealloc {
    [viewController release];
    [window release];
    [super dealloc];
}


@end
