//
//  BaseView.m
//  CampusMaps
//
//  Created by Joshua Stein on 10/15/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "BaseViewController.h"


@implementation BaseViewController

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	
	MapViewController *mapVC = (MapViewController *)self.navigationController.visibleViewController;
	mapVC.managedObjectContext = [self managedObjectContext[;
/*	
	//shifted the center of the base view up 10 pixels
	
	self.view.center = CGPointMake(160.0, 230.0);
	
	mapViewController.managedObjectContext = [self managedObjectContext];
	
	[self.view addSubview:mapViewController.view];
*/}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/


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

@synthesize managedObjectContext;

@end
