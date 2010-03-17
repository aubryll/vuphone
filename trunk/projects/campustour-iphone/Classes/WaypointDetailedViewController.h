//
//  WaypointDetailedViewController.h
//  Campus Tour
//
//  Created by sma1 on 2/23/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Waypoint.h>


@interface WaypointDetailedViewController : UIViewController {

	IBOutlet UITextView *description;
	IBOutlet UILabel *number;
	IBOutlet UILabel *theName;
	IBOutlet UITabBarController *tabBar;
	Waypoint *waypoint;
}

@property (retain) Waypoint *waypoint;


@end
