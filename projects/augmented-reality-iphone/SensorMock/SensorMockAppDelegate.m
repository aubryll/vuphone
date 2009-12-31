//
//  SensorMockAppDelegate.m
//  SensorMock
//
//  Created by Ben Gotow on 11/4/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "SensorMockAppDelegate.h"
#import "SensorMockViewController.h"
#import "SensorMock.h"

@implementation SensorMockAppDelegate

@synthesize window;
@synthesize viewController;


- (void)applicationDidFinishLaunching:(UIApplication *)application {    
    
    StartSensorMock();
    
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
