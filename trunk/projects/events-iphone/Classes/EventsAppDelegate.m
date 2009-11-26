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
	NSLog(@"Getting events");

	// Get events from the server
	[RemoteEventLoader getEventsFromServerSince:[NSDate date] intoContext:context];
	NSLog(@"RemoteEventLoader returned");
	NSError *err = nil;
	[context save:&err];
	
	[pool release];
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
