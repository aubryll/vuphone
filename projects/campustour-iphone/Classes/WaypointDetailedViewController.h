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
	IBOutlet UIButton	*playButton;
	IBOutlet UIButton	*pauseButton;
	IBOutlet UIButton	*stopButton;
	Waypoint *waypoint;
}

@property (retain) Waypoint *waypoint;
@property (nonatomic, retain) UIButton *playButton;
@property (nonatomic, retain) UIButton *pauseButton;
@property (nonatomic, retain) UIButton *stopButton;

- (IBAction)showClasses:(id)sender;
- (IBAction)playButtonTapped:(id)sender;
- (IBAction)pauseButtonTapped:(id)sender;
- (IBAction)stopButtonTapped:(id)sender;

@end
