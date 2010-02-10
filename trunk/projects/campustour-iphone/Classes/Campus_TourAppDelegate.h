//
//  Campus_TourAppDelegate.h
//  Campus Tour
//
//  Created by Aaron Thompson on 2/9/10.
//  Copyright Vanderbilt University 2010. All rights reserved.
//

@interface Campus_TourAppDelegate : NSObject <UIApplicationDelegate> {
    
    UIWindow *window;
    UINavigationController *navigationController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UINavigationController *navigationController;

@end

