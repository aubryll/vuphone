//
//  AboutViewController.m
//  Events
//
//  Created by Aaron Thompson on 1/23/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "AboutViewController.h"
#import "EventsAppDelegate.h"

@implementation AboutViewController

- (IBAction)hide:(id)sender
{
	EventsAppDelegate *appDelegate = (EventsAppDelegate *)[[UIApplication sharedApplication] delegate];
	[appDelegate toggleAboutView:sender];
}

@end
