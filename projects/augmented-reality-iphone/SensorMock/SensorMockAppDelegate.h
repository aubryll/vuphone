//
//  SensorMockAppDelegate.h
//  SensorMock
//
//  Created by Ben Gotow on 11/4/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SensorMockViewController;

@interface SensorMockAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    SensorMockViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet SensorMockViewController *viewController;

@end

