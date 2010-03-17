//
//  WaypointDetailedViewController.m
//  Campus Tour
//
//  Created by sma1 on 2/23/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "WaypointDetailedViewController.h"


@implementation WaypointDetailedViewController

@synthesize waypoint;

 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
//- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
//    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
//        // Custom initialization
//		self.title = @"Detailed Information";
//    }
//    return self;
//}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	self.title = self.waypoint.name;
	theName.text = self.waypoint.name;
	description.text = self.waypoint.description;
	number.text = [NSString stringWithFormat:@"%d", self.waypoint.num];
}

/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)viewDidLoad
{
	self.view = tabBar.view;
}


- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
	self.waypoint = nil;
    [super dealloc];
}

@end
