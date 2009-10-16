//
//  BaseView.m
//  CampusMaps
//
//  Created by Joshua Stein on 10/15/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "BaseViewController.h"


@implementation BaseViewController

/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}
*/


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	
	[self.view addSubview:mapViewController.view];
	mapViewRunning = YES;	
	
	[self instantiateFlipViewButton];
}
	

- (void)instantiateFlipViewButton {
	
	flipViewButton = [UIButton buttonWithType:UIButtonTypeInfoDark];
	
	CGRect buttonRect = flipViewButton.frame;
	
	// calculate the bottom right corner
	buttonRect.origin.x = buttonRect.size.width - 8;
	buttonRect.origin.y = buttonRect.size.height - 8;
	[flipViewButton setFrame:buttonRect];
	
	[flipViewButton addTarget:self action:@selector(didPressFlipViewButton) forControlEvents:UIControlEventTouchUpInside];
	[flipViewButton setEnabled:YES];
	
	[mapViewController.view addSubview:flipViewButton];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/


- (void)didPressFlipViewButton {
	
	[UIView beginAnimations:@"Flipping" context:nil];
	[UIView setAnimationDuration:1.0f];
	
	[flipViewButton removeFromSuperview];
	
	if(mapViewRunning) {
		[UIView setAnimationTransition:UIViewAnimationTransitionFlipFromLeft forView:self.view cache:NO];
		[mapViewController.view removeFromSuperview];
		[self.view addSubview:listViewController.view];
		[listViewController.view addSubview:flipViewButton];
		mapViewRunning = NO;
		
	}
	else {
		[UIView setAnimationTransition:UIViewAnimationTransitionFlipFromRight forView:self.view cache:NO];
		[listViewController.view removeFromSuperview];
		[self.view addSubview:mapViewController.view];
		[mapViewController.view addSubview:flipViewButton];
		mapViewRunning = YES;
		
	}
		
	[UIView commitAnimations];

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


@end
