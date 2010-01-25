//
//  AboutViewController.m
//  CampusMaps
//
//  Created by Aaron Thompson on 1/23/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "AboutViewController.h"
#import "CampusMapsAppDelegate.h"

@implementation AboutViewController

- (IBAction)hide:(id)sender
{
	CampusMapsAppDelegate *appDelegate = (CampusMapsAppDelegate *)[[UIApplication sharedApplication] delegate];
	[appDelegate toggleAboutView];
}

@end
