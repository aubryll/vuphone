//
//  MapViewController.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <CoreData/CoreData.h>

#import "MapFilterViewController.h"

@interface MapViewController : UIViewController <MKMapViewDelegate, NSFetchedResultsControllerDelegate> {

	NSManagedObjectContext *managedObjectContext;
	NSFetchedResultsController *fetchedResultsC;
	IBOutlet MKMapView *mapView;
	IBOutlet MapFilterViewController *mapFilterVC;

}

- (IBAction)showFilterSheet:(id)sender;

@property (nonatomic, retain) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, retain) NSFetchedResultsController *fetchedResultsC;

@end
