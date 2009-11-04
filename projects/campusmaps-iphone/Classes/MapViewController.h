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

@interface MapViewController : UIViewController <MKMapViewDelegate> {

	MapLayerController* exampleLayer;
	
	IBOutlet UISegmentedControl* mapType;
	IBOutlet MKMapView* mapView;
	NSManagedObjectContext* managedObjectContext;
}

- (IBAction)changeType:(id) sender;

@property (retain) NSManagedObjectContext *managedObjectContext;

@end
