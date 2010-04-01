//
//  MapViewController.h
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreData/CoreData.h>

#import "MapFilterViewController.h"

@interface MapViewController : UIViewController <MKMapViewDelegate, NSFetchedResultsControllerDelegate> {

	NSManagedObjectContext *managedObjectContext;
	NSFetchedResultsController *fetchedResultsC;
	NSPredicate *filterPredicate;
	
	IBOutlet MKMapView *mapView;
	IBOutlet MapFilterViewController *mapFilterVC;
	
	BOOL showingFilterSheet;
}

- (IBAction)toggleFilterSheet:(id)sender;
- (void)filterPredicateChanged:(NSNotification *)notification;
- (void)refetch;
- (IBAction)centerOnUserLocation:(id)sender;

@property (retain) NSManagedObjectContext *managedObjectContext;
@property (retain) NSFetchedResultsController *fetchedResultsC;
@property (retain) NSPredicate *filterPredicate;

@end
