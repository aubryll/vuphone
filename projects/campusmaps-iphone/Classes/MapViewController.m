//
//  MapViewController.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapViewController.h"
#import "MapLayerController.h"


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
	
	MKCoordinateRegion region;
	MKCoordinateSpan span;
	span.longitudeDelta = 0.01;
	span.latitudeDelta = 0.01;
	
	CLLocationCoordinate2D location;
	
	location.latitude = CAMPUS_CENTER_LATITUDE;
	location.longitude = CAMPUS_CENTER_LONGITUDE;
	
	exampleLayer = [[MapLayerController alloc] initWithCoordinate:location objectContext:managedObjectContext];
	
	region.span = span;
	region.center = location;

	[mapView setRegion:region animated:TRUE];
	[mapView regionThatFits:region];
	[exampleLayer addAnnotationsToMapView:mapView];
	
	//[self.view insertSubview:mapView atIndex:0];

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

- (MKAnnotationView *) mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	MKPinAnnotationView* annView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"currentloc"];
	annView.animatesDrop = YES;
	return annView;
}

- (void)dealloc {
	[mapView autorelease];
	[exampleLayer autorelease];
    [super dealloc];
}


@synthesize managedObjectContext;

@end
