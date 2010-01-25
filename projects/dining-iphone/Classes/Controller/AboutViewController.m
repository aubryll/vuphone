//
//  AboutViewController.m
//  CampusMaps
//
//  Created by Aaron Thompson on 1/23/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "AboutViewController.h"
#import "DiningAppDelegate.h"

@implementation AboutViewController

- (IBAction)hide:(id)sender
{
	DiningAppDelegate *appDelegate = (DiningAppDelegate *)[[UIApplication sharedApplication] delegate];
	[appDelegate toggleAboutView:sender];
}

@end
