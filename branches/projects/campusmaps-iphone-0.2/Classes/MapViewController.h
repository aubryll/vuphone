//
//  MapViewController.h
//  CampusMaps
//
//  Created by Ben Wibking on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <MapKit/MKAnnotation.h>
#import "MapLayerController.h"
#import "LayersListViewController.h"

@interface MapViewController : UIViewController <MKMapViewDelegate, LayersListViewDelegate> {

	MapLayerController* currentLayerController;
	
	IBOutlet UISegmentedControl* mapType;
	IBOutlet MKMapView* mapView;
	NSManagedObjectContext* managedObjectContext;
}

- (IBAction)changeType:(id)sender;
- (IBAction)showLayersSheet:(id)sender;
- (IBAction)centerOnCampus:(id)sender;

@property (retain) NSManagedObjectContext *managedObjectContext;

@end
