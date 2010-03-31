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
	IBOutlet UIImageView *theImage;
	IBOutlet UITabBarController *tabBar;
	IBOutlet UITextView *funFacts;
	IBOutlet UITextView *testimonials;
	Waypoint *waypoint;
}

@property (retain) Waypoint *waypoint;
-(IBAction)showClasses:(id)sender;

@end
