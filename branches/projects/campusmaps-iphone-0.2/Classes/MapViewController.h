//
//  MapViewController.h
//  CampusMaps
//
//  Created by Ben Wibking on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <UIKit/UIImage.h>
#import <MapKit/MapKit.h>
#import <MapKit/MKAnnotation.h>
#import "MapLayerController.h"
#import "LayersListViewController.h"

// Image from http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=|FFCC66|000000
#define ANNOTATION_IMAGE_FILE @"Images/pin-brown.png"

@interface MapViewController : UIViewController <MKMapViewDelegate, UISearchBarDelegate, LayersListViewDelegate> {

	MapLayerController *currentLayerController;
	
	IBOutlet UISegmentedControl* mapType;
	IBOutlet MKMapView* mapView;
	IBOutlet UIBarButtonItem* homeButton;
	IBOutlet UIBarButtonItem* showLayersButton;
	
	UIImage* annotationImage;
	
	NSManagedObjectContext* managedObjectContext;
}

- (IBAction)changeType:(id)sender;
- (IBAction)showLayersSheet:(id)sender;
- (IBAction)centerOnCampus:(id)sender;
- (IBAction)toggleAboutView:(id)sender;

@property (retain) NSManagedObjectContext *managedObjectContext;

@end
