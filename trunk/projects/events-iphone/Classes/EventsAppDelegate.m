//
//  EventsAppDelegate.m
//  Events
//
//  Created by Aaron Thompson on 9/6/09.
//  Copyright Vanderbilt University 2009. All rights reserved.
//

#import "EventsAppDelegate.h"
#import "RemoteEventLoader.h"
#import "EventStore.h"

@implementation EventsAppDelegate

@synthesize window;


#pragma mark -
#pragma mark Application lifecycle

- (void)applicationDidFinishLaunching:(UIApplication *)application
{
	eventListVC.navigationItem.leftBarButtonItem = aboutButton;

	NSManagedObjectContext *context = [[EventStore sharedEventStore] sharedContext];
	if (!context) {
		NSLog(@"Failed to get managed object context!");
	}
	
	eventListVC.context = context;
	mapVC.managedObjectContext = context;

	[window addSubview:[tabBarC view]];
	[window makeKeyAndVisible];
	
	[self performSelectorInBackground:@selector(getEventsSinceLastUpdate:) withObject:context];
}

- (void)getEventsSinceLastUpdate:(NSManagedObjectContext *)context
{
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];

	NSDate *lastUpdate = [[NSUserDefaults standardUserDefaults] objectForKey:DefaultsLastUpdateKey];

	if (lastUpdate == nil)
	{
		lastUpdate = [NSDate dateWithTimeIntervalSince1970:0];

		// Show the loading screen
		[self performSelectorOnMainThread:@selector(showLoadingView)
							   withObject:nil
							waitUntilDone:NO];
		
		// Get our events for the first time.
		NSArray *events = [RemoteEventLoader getEventsFromServerSince:lastUpdate intoContext:context];
		
		// If we get an error trying to get the events for the first time, we should
		// exit until they can try again and get it working.
		if (events == nil) {
			for (UIView *view in loadingViewController.view.subviews) {
				if ([view isKindOfClass: [UILabel class]]) {
					[(UILabel *)view setText: @"Sorry, I couldn't download the latest events. \
					 Please try again later. Thanks!"];
				} else if ([view isKindOfClass: [UIActivityIndicatorView class]]) {
					[(UIActivityIndicatorView *)view stopAnimating];
				}
			}
			//[loadingViewController.view removeFromSuperview];
			[pool release];
			return;
		}
	} else {
		[RemoteEventLoader getEventsFromServerSince:lastUpdate intoContext:context];
	}
	
	
	NSError *err = nil;
	[context save:&err];
	
	if ([lastUpdate timeIntervalSince1970] == 0) {
		// Hide the loading screen
		[loadingViewController.view removeFromSuperview];
	}
	
	if (err == nil) {
		// Store that now was the last update
		[[NSUserDefaults standardUserDefaults] setObject:[NSDate date] forKey:DefaultsLastUpdateKey];
	}
	
	[pool release];
}

- (void)showLoadingView
{
	// Overlay a loading screen
	NSLog(@"Displaying loading view");
	loadingViewController = [[UIViewController alloc] initWithNibName:@"LoadingViewController" bundle:nil];
	CGRect frame = loadingViewController.view.frame;
	frame.origin.y += 20.0f;
	loadingViewController.view.frame = frame;
	[self.window addSubview:loadingViewController.view];
}

- (IBAction)toggleAboutView:(id)sender
{
	[UIView beginAnimations:@"AboutViewTransition" context:NULL];
	[UIView setAnimationDuration:0.5];
	
	if (!aboutViewShowing)
	{
		aboutViewController = [[AboutViewController alloc] initWithNibName:@"AboutViewController" bundle:nil];
		// Offset the view down by 20 pixels, the height of the status bar
		aboutViewController.view.frame = CGRectMake(0, 20, 320, 460);
		
		[UIView setAnimationTransition:UIViewAnimationTransitionFlipFromRight forView:self.window cache:NO];
		[self.window addSubview:aboutViewController.view];
	}
	else
	{
		[UIView setAnimationTransition:UIViewAnimationTransitionFlipFromLeft forView:self.window cache:NO];
		[aboutViewController.view removeFromSuperview];
	}
	
	aboutViewShowing = !aboutViewShowing;
	[UIView commitAnimations];
}


- (void)applicationWillTerminate:(UIApplication *)application {
	// Nothing to save here
}


#pragma mark -
#pragma mark Saving

/**
 Performs the save action for the application, which is to send the save:
 message to the application's managed object context.
 */
- (IBAction)saveAction:(id)sender
{
	NSError *error;
	if (![[[EventStore sharedEventStore] sharedContext] save:&error]) {
		// Handle error
		NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
		exit(-1);  // Fail
	}
}



#pragma mark -
#pragma mark Memory management

- (void)dealloc {
	
	[window release];
	[super dealloc];
}


@end
