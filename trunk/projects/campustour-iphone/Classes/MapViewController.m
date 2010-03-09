//
//  MapViewController.m
//  Campus Tour
//
//  Created by Guy on 2/16/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "MapViewController.h"
#import "Waypoint.h"
#import "WaypointDetailedViewController.h"
#import "../KissXML/DDXMLDocument.h"
#import "WaypointXMLReader.h"

@implementation MapViewController

/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}
*/


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	[self centerOnCampus:nil];
	self.title = @"Campus Map";
	NSData *iconData = [NSData dataWithContentsOfFile:[[[NSBundle mainBundle] resourcePath]
													   stringByAppendingPathComponent:ANNOTATION_IMAGE_FILE]];
	annotationImage = [[UIImage alloc] initWithData:iconData];
	
	NSArray *waypoints = [WaypointXMLReader waypointsFromXMLAtPath:[[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:POI_REQUEST_ALTERNATIVE]];
	
	for (int i = 0; i < [waypoints count]; i++)
	{
	    [mapView addAnnotation:[waypoints objectAtIndex:i]];
	}

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

	
}

- (IBAction)centerOnCampus:(id)sender {
	CLLocationCoordinate2D location;
	location.latitude = 36.142;
	location.longitude = -86.8044;
	
	MKCoordinateRegion region;
	MKCoordinateSpan span;
	span.longitudeDelta = 0.01;
	span.latitudeDelta = 0.01;
	
	region.span = span;
	region.center = location;
	
	[mapView setRegion:region animated:TRUE];
	[mapView regionThatFits:region];
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	//self.navigationItem.backBarButtonItem.title = @"Back";
}

- (void)dealloc {
    [super dealloc];
}


- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	static NSString *reuseIdentifier = @"reusedAnnView";
	MKAnnotationView* annView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
	annView.image = annotationImage;
	annView.canShowCallout = YES;
	annView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
	
	return [annView autorelease];	
}


-(void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
	Waypoint *waypoint = (Waypoint *) view.annotation;
	WaypointDetailedViewController *newViewController = [[WaypointDetailedViewController alloc] initWithNibName:@"WaypointDetailedViewController" bundle:nil];
	newViewController.waypoint = waypoint;
	[self.navigationController pushViewController:newViewController animated:TRUE];
	[newViewController release];
}


@end
