//
//  MapViewController.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "MapViewController.h"
#import "Event.h"
#import "Location.h"
#import "EntityConstants.h"

@implementation MapViewController

- (void)viewWillAppear:(BOOL)animated
{
	if (!fetchedResultsC)
	{
		// Set up the fetch request
		NSFetchRequest *request = [[NSFetchRequest alloc] init];
		NSEntityDescription *entity = [NSEntityDescription entityForName:@"Event" inManagedObjectContext:managedObjectContext];
		[request setEntity:entity];
		
		// Set the sort descriptor, because NSFetchedResultsController sends messages to freed objects if not
		NSSortDescriptor *sort = [[NSSortDescriptor alloc] initWithKey:VUEntityPropertyNameName ascending:YES];
		[request setSortDescriptors:[NSArray arrayWithObject:sort]];
		[sort release];

		// Set up the fetched results controller
		fetchedResultsC = [[NSFetchedResultsController alloc]
						   initWithFetchRequest:request
						   managedObjectContext:managedObjectContext
						   sectionNameKeyPath:nil
						   cacheName:@"mapEventsCache"];
		[request release];
		// Set self as the delegate
		fetchedResultsC.delegate = self;
		
		// Execute the request
		NSError *error;
		BOOL success = [fetchedResultsC performFetch:&error];
		if (!success) {
			NSLog(@"No events found");
		} else {
			// Add the annotations
			for (Event *event in [fetchedResultsC fetchedObjects]) {
				[mapView addAnnotation:event];
			}
		}

		// Move the map to VU's campus
		CLLocationCoordinate2D coords;
		coords.latitude = CAMPUS_CENTER_LATITUDE;
		coords.longitude = CAMPUS_CENTER_LONGITUDE;
		[mapView setRegion:MKCoordinateRegionMakeWithDistance(coords, 400.0, 400.0)];
	}
}

// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait ||
			interfaceOrientation == UIInterfaceOrientationLandscapeLeft ||
			interfaceOrientation == UIInterfaceOrientationLandscapeRight);
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
	[fetchedResultsC release];
    [super dealloc];
}

#pragma mark NSFetchedResultsControllerDelegate

- (void)controller:(NSFetchedResultsController *)controller didChangeObject:(id)anObject
	   atIndexPath:(NSIndexPath *)indexPath forChangeType:(NSFetchedResultsChangeType)type
	  newIndexPath:(NSIndexPath *)newIndexPath
{
	switch(type)
	{
		case NSFetchedResultsChangeInsert:
			NSLog(@"NSFetchedResultsChangeInsert with event %@", ((Event *)anObject).name);
			[mapView addAnnotation:anObject];
			break;
			
		case NSFetchedResultsChangeDelete:
			NSLog(@"NSFetchedResultsChangeDelete");
			[mapView removeAnnotation:anObject];
			break;
			
		case NSFetchedResultsChangeUpdate:
			NSLog(@"NSFetchedResultsChangeUpdate");
			[mapView removeAnnotation:anObject];
			[mapView addAnnotation:anObject];
			break;
			
		case NSFetchedResultsChangeMove:
			break;
	}
}


@synthesize managedObjectContext;
@synthesize fetchedResultsC;

@end
