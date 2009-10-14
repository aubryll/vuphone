//
//  iPhoneARAppDelegate.h
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright Gotow.net Creative 2009. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ARViewController;

@interface iPhoneARAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
    ARViewController *viewController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet ARViewController *viewController;

@end

