//
//  MapViewController.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapViewController.h"
#import "POI.h"
#import "Layer.h"

@implementation MapViewController


 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	// mapView = [[MKMapView alloc] initWithFrame:self.view.bounds];
	
	//mapView.showsUserLocation = TRUE;
	//mapView.mapType = MKMapTypeStandard;
	// mapView.delegate = self;
	

	Layer *anyLayer = [[Layer allLayers:managedObjectContext] anyObject];
	currentLayerController = [[MapLayerController alloc] initWithLayer:anyLayer];

	CLLocationCoordinate2D location;
	location.latitude = CAMPUS_CENTER_LATITUDE;
	location.longitude = CAMPUS_CENTER_LONGITUDE;

	MKCoordinateRegion region;
	MKCoordinateSpan span;
	span.longitudeDelta = 0.01;
	span.latitudeDelta = 0.01;
	
	region.span = span;
	region.center = location;

	[mapView setRegion:region animated:TRUE];
	[mapView regionThatFits:region];
	[currentLayerController addAnnotationsToMapView:mapView];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (IBAction)changeType:(id)sender {
	NSLog(@"Button pushed.");
	
	if (mapType.selectedSegmentIndex == 0) {
		mapView.mapType = MKMapTypeStandard;
	} else if (mapType.selectedSegmentIndex == 1) {
		mapView.mapType = MKMapTypeSatellite;
	} else if (mapType.selectedSegmentIndex == 2) {
		mapView.mapType = MKMapTypeHybrid;
	}
}

- (IBAction)centerOnCampus:(id)sender {
	NSLog(@"Button pushed.");
	
	CLLocationCoordinate2D location;
	location.latitude = CAMPUS_CENTER_LATITUDE;
	location.longitude = CAMPUS_CENTER_LONGITUDE;
	
	MKCoordinateRegion region;
	MKCoordinateSpan span;
	span.longitudeDelta = 0.01;
	span.latitudeDelta = 0.01;
	
	region.span = span;
	region.center = location;
	
	[mapView setRegion:region animated:TRUE];
	[mapView regionThatFits:region];

}

#pragma mark Layers

- (IBAction)showLayersSheet:(id)sender
{
	LayersListViewController *layersVC = [[LayersListViewController alloc] initWithNibName:@"LayersListViewController" bundle:nil];
	layersVC.delegate = self;
	layersVC.layers = [[Layer allLayers:managedObjectContext] allObjects];
	[self presentModalViewController:layersVC animated:YES];
	
	[layersVC release];
}

- (void)layersListViewController:(LayersListViewController *)layersListVC
				  didChooseLayer:(Layer *)layer
{
	// Adjust the query to include these choices
	[currentLayerController removeAnnotationsFromMapView:mapView];
	[currentLayerController release];
	currentLayerController = [[MapLayerController alloc] initWithLayer:layer];
	[currentLayerController addAnnotationsToMapView:mapView];
}


- (MKAnnotationView *) mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	MKPinAnnotationView* annView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"currentloc"];
	annView.animatesDrop = YES;
	annView.userInteractionEnabled = YES;
	annView.canShowCallout = YES;
	return [annView autorelease];
}

- (void)dealloc {
	[mapView autorelease];
	[currentLayerController autorelease];
    [super dealloc];
}


@synthesize managedObjectContext;

@end
