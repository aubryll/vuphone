//
//  AboutViewController.m
//  Commencement
//
//  Created by Aaron Thompson on 1/23/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "AboutViewController.h"
#import "CommencementAppDelegate.h"

@implementation AboutViewController

- (IBAction)hide:(id)sender
{
	CommencementAppDelegate *appDelegate = (CommencementAppDelegate *)[[UIApplication sharedApplication] delegate];
	[appDelegate toggleAboutView:sender];
}

@end
