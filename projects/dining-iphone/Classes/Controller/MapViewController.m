//
//  MapViewController.m
//  Dining
//
//  Created by Aaron Thompson on 1/11/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "MapViewController.h"
#import "RestaurantViewController.h"
#import "DiningAppDelegate.h"
#import "NSManagedObjectContext-Convenience.h"

@implementation MapViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	
	CLLocationCoordinate2D location;
	location.latitude = CAMPUS_CENTER_LATITUDE;
	location.longitude = CAMPUS_CENTER_LONGITUDE;
	
	MKCoordinateRegion region;
	MKCoordinateSpan span;
	span.longitudeDelta = 0.012;
	span.latitudeDelta = 0.010;
	
	region.span = span;
	region.center = location;
	
	[mapView setRegion:region animated:TRUE];

	
	DiningAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
	NSSet *restaurants = [[appDelegate managedObjectContext] fetchObjectsForEntityName:ENTITY_NAME_RESTAURANT
																   withPredicateString:@"TRUEPREDICATE"];
	[mapView addAnnotations:[restaurants allObjects]];
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
	MKPinAnnotationView *annView = [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"annotationView"];
	annView.animatesDrop = NO;
	annView.userInteractionEnabled = YES;
	annView.canShowCallout = YES;
	annView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
	annView.calloutOffset = CGPointMake(-5, 5);

	return [annView autorelease];
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control
{
	Restaurant *restaurant = (Restaurant *)view.annotation;
	
	// Push the restaurant detail view controller
	RestaurantViewController *restaurantVC = [[RestaurantViewController alloc] initWithNibName:@"RestaurantViewController" bundle:nil];
	restaurantVC.restaurant = restaurant;
	restaurantVC.title = restaurant.name;
	
	[self.navigationController pushViewController:restaurantVC animated:YES];
	[restaurantVC release];
}

- (void)selectRestaurant:(Restaurant *)restaurant
{
	[mapView setCenterCoordinate:restaurant.coordinate animated:YES];
	[mapView selectAnnotation:restaurant animated:YES];
}

// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
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
    [super dealloc];
}


@end
