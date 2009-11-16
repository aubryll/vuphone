//
//  MapViewController.m
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "MapViewController.h"
#import "EventViewController.h"
#import "Event.h"
#import "Location.h"
#import "EntityConstants.h"

@implementation MapViewController


// Shows or hides the filter sheet
- (IBAction)toggleFilterSheet:(id)sender
{
	if (showingFilterSheet)
	{
		// Hide the filter sheet
		[UIView beginAnimations:@"filterSheet" context:nil];
		mapFilterVC.view.frame = CGRectMake(0.0f,
											-FILTER_SHEET_HEIGHT,
											mapFilterVC.view.frame.size.width,
											FILTER_SHEET_HEIGHT);
		[UIView commitAnimations];
	}
	else
	{
		// Add the filter sheet
		[UIView beginAnimations:@"filterSheet" context:nil];
		[UIView setAnimationTransition:UIViewAnimationCurveEaseInOut forView:mapFilterVC.view cache:NO];
		mapFilterVC.view.frame = CGRectMake(0.0f,
											0.0f,
											mapFilterVC.view.frame.size.width,
											FILTER_SHEET_HEIGHT);
		
		[UIView commitAnimations];
	}
	
	showingFilterSheet = !showingFilterSheet;
}

// Responds to the VU_MAPFILTER_CHANGED_NOTIFICATION, sets the new filter predicate, and refetches
- (void)filterPredicateChanged:(NSNotification *)notification
{
	self.filterPredicate = [notification object];
	[fetchedResultsC.fetchRequest setPredicate:self.filterPredicate];

	[self refetch];
}

// (Re-)executes the fetch in the fetched results controller, then refreshes the annotations
- (void)refetch
{
	if (fetchedResultsC)
	{
		// Execute the request
		NSError *error;
		BOOL success = [fetchedResultsC performFetch:&error];
		if (!success) {
			NSLog(@"No events found");
		} else {
			// Remove any old annotations
			[mapView removeAnnotations:[mapView annotations]];
			
			// Add the annotations
			for (Event *event in [fetchedResultsC fetchedObjects]) {
				[mapView addAnnotation:event];
			}
		}
	}
}


// Scrolls the map to center on the user's location
- (IBAction)centerOnUserLocation:(id)sender
{
	MKUserLocation *userLocation = [mapView userLocation];
	[mapView setCenterCoordinate:userLocation.coordinate animated:YES];
}

- (void)viewDidLoad
{
	// Subscribe to the filter changed notification
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(filterPredicateChanged:)
												 name:VU_MAPFILTER_CHANGED_NOTIFICATION
											   object:nil];
	
	// Set up the filter sheet
	showingFilterSheet = NO;
	mapFilterVC.view.frame = CGRectMake(0.0f,
										-FILTER_SHEET_HEIGHT,
										mapFilterVC.view.frame.size.width,
										FILTER_SHEET_HEIGHT);

	[self.view addSubview:mapFilterVC.view];

	// Move the map to VU's campus
	CLLocationCoordinate2D coords;
	coords.latitude = CAMPUS_CENTER_LATITUDE;
	coords.longitude = CAMPUS_CENTER_LONGITUDE;
	[mapView setRegion:MKCoordinateRegionMakeWithDistance(coords, 400.0, 400.0)];
}

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
		
		[request setPredicate:[mapFilterVC predicate]];

		// Set up the fetched results controller
		fetchedResultsC = [[NSFetchedResultsController alloc]
						   initWithFetchRequest:request
						   managedObjectContext:managedObjectContext
						   sectionNameKeyPath:nil
						   cacheName:@"mapEventsCache"];
		[request release];
		// Set self as the delegate
		fetchedResultsC.delegate = self;
		
		[self refetch];
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

#pragma mark MKMapViewDelegate

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation
{
	MKPinAnnotationView *annotationView;

	// Special case for the current location
	if ([annotation isKindOfClass:[MKUserLocation class]]) {
		annotationView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"userLocation"];
		annotationView.pinColor = MKPinAnnotationColorRed;
		annotationView.canShowCallout = NO;
		annotationView.userInteractionEnabled = NO;
	} else {
		annotationView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"eventLocation"];
		annotationView.pinColor = MKPinAnnotationColorGreen;
		annotationView.canShowCallout = YES;
		annotationView.calloutOffset = CGPointMake(-5, 5);
		annotationView.userInteractionEnabled = YES;
		annotationView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
	}
	
    return [annotationView autorelease];
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
	Event *event = (Event *)view.annotation;
	
	// Push the event detail view controller
	EventViewController *eventVC = [[EventViewController alloc] initWithNibName:@"EventView" bundle:nil];
	eventVC.event = event;
	eventVC.context = managedObjectContext;
	eventVC.title = event.name;

	[self.navigationController pushViewController:eventVC animated:YES];
	[eventVC release];
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
@synthesize filterPredicate;

@end
