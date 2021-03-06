//
//  WaypointDetailedViewController.m
//  Campus Tour
//
//  Created by sma1 on 2/23/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "WaypointDetailedViewController.h"
#import "SitOnClassViewController.h"
#import "AudioManager.h"

@implementation WaypointDetailedViewController

@synthesize waypoint;
@synthesize playButton;
@synthesize pauseButton;
@synthesize stopButton;

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
	theImage.image = self.waypoint.image;
	funFacts.text = self.waypoint.funFacts;
	testimonials.text = self.waypoint.testimonials;
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

-(IBAction)showClasses:(id)sender
{
	SitOnClassViewController *newController = [[SitOnClassViewController alloc] init];
	[self.navigationController pushViewController:newController animated:YES];
	[newController release];
}

#pragma mark -
#pragma mark Audio Playback Methods
// Play the audio file associated with the waypoint you selected. 
- (IBAction)playButtonTapped:(id)sender {	
	if ([[AudioManager sharedAudioManager] audioPlayerURL] == nil || 
		[[AudioManager sharedAudioManager] currentObject] != sender) {

		[[AudioManager sharedAudioManager] playAudioFile: waypoint.audioFilePath 
												  ofType: @"mp3" 
											  withSender: sender];
	} else {
		[[AudioManager sharedAudioManager] resumePlayback];
	}
	
}

- (IBAction)pauseButtonTapped:(id)sender {
	[[AudioManager sharedAudioManager] pausePlayback];
}

- (IBAction)stopButtonTapped:(id)sender {
	[[AudioManager sharedAudioManager] stopAndReleasePlayer];
}

@end
