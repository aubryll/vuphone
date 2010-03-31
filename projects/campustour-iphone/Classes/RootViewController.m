//
//  RootViewController.m
//  Campus Tour
//
//  Created by Aaron Thompson on 2/9/10.
//  Copyright Vanderbilt University 2010. All rights reserved.
//

#import "RootViewController.h"
#import "MapViewController.h"
#import "AudioManager.h"
#import "SitOnClassViewController.h"

@implementation RootViewController

/*
- (void)viewDidLoad {
    [super viewDidLoad];

    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}
*/

/*
- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}
*/
/*
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
}
*/
/*
- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
}
*/
/*
- (void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
}
*/

/*
 // Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
	// Return YES for supported orientations.
	return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
 */

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release anything that can be recreated in viewDidLoad or on demand.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}


- (IBAction)startTour:(id)sender {
	MapViewController * mapViewController = [[MapViewController alloc] initWithNibName:@"MapViewController" bundle:nil];
	[self.navigationController pushViewController:mapViewController animated:YES];
	[[AudioManager sharedAudioManager] playAudioFile:@"inMySpaghetti" ofType:@"mp3"];
	[mapViewController release];
}

- (IBAction)showAboutVandy:(id)sender {
	NSLog(@"showAboutVandy tapped");
}

- (IBAction)showSitInOnClass:(id)sender {
	SitOnClassViewController *controller = [[SitOnClassViewController alloc] initWithNibName:@"SitOnClassViewController" bundle:nil];
	[self.navigationController pushViewController:controller animated:YES];
	[controller release];
}

- (IBAction)showTalkToSomeone:(id)sender {
	NSLog(@"showTalkToSomeone tapped");
}

@end

