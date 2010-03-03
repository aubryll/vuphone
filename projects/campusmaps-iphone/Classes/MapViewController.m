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
#import "POIViewController.h"

@implementation MapViewController


 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
	if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
		// Custom initialization
	}
	return self;
}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
	
	self.navigationItem.leftBarButtonItem = homeButton;
//	self.navigationItem.rightBarButtonItem = showLayersButton;
	self.navigationItem.title = @"Campus Maps";

	Layer *anyLayer = [[Layer allLayers:managedObjectContext] anyObject];
	currentLayerController = [[MapLayerController alloc] initWithLayer:anyLayer andMapView:mapView];
	
	
	CLLocationCoordinate2D location;
	location.latitude = CAMPUS_CENTER_LATITUDE;
	location.longitude = CAMPUS_CENTER_LONGITUDE;

	MKCoordinateRegion region;
	MKCoordinateSpan span;
	span.longitudeDelta = 0.01;
	span.latitudeDelta = 0.01;
	
	region.span = span;
	region.center = location;
	
	NSData *iconData = [NSData dataWithContentsOfFile:[[[NSBundle mainBundle] resourcePath] 
													   stringByAppendingPathComponent:ANNOTATION_IMAGE_FILE]];
	annotationImage = [[UIImage alloc] initWithData:iconData];
	NSAssert(annotationImage != nil, @"The annotation icon file cannot be loaded.");

	[mapView setRegion:region animated:TRUE];
	[mapView regionThatFits:region];
	[currentLayerController addAnnotationsToMapView];
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


- (void)dealloc {
	[mapView autorelease];
	[currentLayerController autorelease];
	[annotationImage autorelease];
	
    [super dealloc];
}

#pragma mark IBActions

- (IBAction)changeType:(id)sender {
	if (mapType.selectedSegmentIndex == 0) {
		mapView.mapType = MKMapTypeStandard;
	} else if (mapType.selectedSegmentIndex == 1) {
		mapView.mapType = MKMapTypeSatellite;
	} else if (mapType.selectedSegmentIndex == 2) {
		mapView.mapType = MKMapTypeHybrid;
	}
}

- (IBAction)centerOnCampus:(id)sender {
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

- (IBAction)toggleAboutView:(id)sender {
	CampusMapsAppDelegate *appDelegate = (CampusMapsAppDelegate *)[[UIApplication sharedApplication] delegate];
	[appDelegate toggleAboutView];
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
	[currentLayerController removeAnnotationsFromMapView];
	[currentLayerController release];
	currentLayerController = [[MapLayerController alloc] initWithLayer:layer andMapView:mapView];
	[currentLayerController addAnnotationsToMapView];
}



#pragma mark MKMapViewDelegate

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
	MKAnnotationView *annView;
	
	if ([annotation isKindOfClass:[POI class]]) {
		static NSString *reuseIdentifier = @"reusedAnnView";
		annView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
		annView.image = annotationImage;
		annView.canShowCallout = YES;
		annView.calloutOffset = CGPointMake(-5.0f, 0.0f);
		annView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
	} else {
		// This is the "Current Location" pin
		static NSString *reuseIdentifier = @"currentLocationIdentifier";
		annView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
		((MKPinAnnotationView *)annView).pinColor = MKPinAnnotationColorRed;
		annView.canShowCallout = YES;
		annView.calloutOffset = CGPointMake(3.0f, 3.0f);
		annView.rightCalloutAccessoryView = nil;
	}

	return [annView autorelease];
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
	POI *poi = (POI *)view.annotation;
	
	// Push the POI detail view controller
	POIViewController *poiVC = [[POIViewController alloc] initWithNibName:@"POIViewController" bundle:nil];
	poiVC.poi = poi;
	poiVC.title = poi.name;
	
	[self.navigationController pushViewController:poiVC animated:YES];
	[poiVC release];
}

#pragma mark UISearchBarDelegate

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText
{
	NSPredicate *pred;
	if (searchText && [searchText length] > 1) {
		pred = [NSPredicate predicateWithFormat:@"searchKeywords contains[c] %@", searchText];
	} else {
		pred = nil;
	}

	[currentLayerController setPredicate:pred forContext:managedObjectContext];
	
	if ([currentLayerController.filteredPOIs count] == 1)
	{
		id<MKAnnotation> ann = [currentLayerController.filteredPOIs anyObject];
		[mapView setCenterCoordinate:ann.coordinate animated:YES];
		[mapView selectAnnotation:ann animated:YES];
	}
	else if ([currentLayerController.filteredPOIs count] != 0)
	{
		// Work around a weird bug where the map view delays showing the new POIs
		[mapView setCenterCoordinate:mapView.centerCoordinate animated:YES];
	}
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar
{
	[currentLayerController setPredicate:nil forContext:managedObjectContext];

	searchBar.text = nil;
	[searchBar resignFirstResponder];
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar
{
	[searchBar resignFirstResponder];
}

@synthesize managedObjectContext;

@end
