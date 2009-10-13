//
//  MapFilterViewController.m
//  Events
//
//  Created by Aaron Thompson on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapFilterViewController.h"


@implementation MapFilterViewController

- (IBAction)dismissSheet:(id)sender
{
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction)startDateSliderValueChanged:(id)sender
{
	
}

- (IBAction)endDateSliderValueChanged:(id)sender
{
	
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
    [super dealloc];
}

@synthesize startDayNumber;
@synthesize endDayNumber;
@dynamic tags;

@end
