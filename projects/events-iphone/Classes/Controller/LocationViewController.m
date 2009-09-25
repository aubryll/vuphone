//
//  LocationViewController.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LocationViewController.h"
#import "EventViewController.h"

@implementation LocationViewController

- (void)viewWillAppear:(BOOL)animated
{
	// Populate the fields from the location
	nameField.text = location.name;
	latitudeField.text = [location.latitude stringValue];
	longitudeField.text = [location.longitude stringValue];
	
	CLLocationCoordinate2D coords;
	coords.latitude = [location.latitude doubleValue];
	coords.longitude = [location.longitude doubleValue];
	[mapView setRegion:MKCoordinateRegionMakeWithDistance(coords, 100.0, 100.0)];
	
	[self applyIsEditing];
}

- (void)viewWillDisappear:(BOOL)animated {
	// Populate the location from the fields
	location.name = nameField.text;
	location.latitude = [NSDecimalNumber decimalNumberWithString:latitudeField.text];
	location.longitude = [NSDecimalNumber decimalNumberWithString:longitudeField.text];
}

- (IBAction)save:(id)sender
{
	location.name = nameField.text;
	location.latitude = [NSDecimalNumber decimalNumberWithString:latitudeField.text];
	location.longitude = [NSDecimalNumber decimalNumberWithString:longitudeField.text];

	// Save changes to the location
	NSError *err;
	[editingContext save:&err];
	
	// Pop all the way to the EventViewController
	for (NSObject *controller in self.navigationController.viewControllers) {
		if ([controller isKindOfClass:[EventViewController class]]) {
			[((EventViewController *)controller).tableView reloadData];
			[self.navigationController popToViewController:(UIViewController *)controller animated:YES];
		}
	}
}

- (void)applyIsEditing
{
	nameField.enabled = isEditing;
	latitudeField.enabled = isEditing;
	longitudeField.enabled = isEditing;
	
	if (isEditing) {
		self.title = @"Edit Location";
		self.navigationItem.rightBarButtonItem = saveButton;

		nameField.borderStyle = UITextBorderStyleRoundedRect;
		latitudeField.borderStyle = UITextBorderStyleRoundedRect;
		longitudeField.borderStyle = UITextBorderStyleRoundedRect;
	} else {
		self.title = @"Location Details";
		self.navigationItem.rightBarButtonItem = nil;

		nameField.borderStyle = UITextBorderStyleNone;
		latitudeField.borderStyle = UITextBorderStyleNone;
		longitudeField.borderStyle = UITextBorderStyleNone;
	}
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

#pragma mark MKMapViewDelegate

- (void)mapView:(MKMapView *)aMapView regionDidChangeAnimated:(BOOL)animated {
	MKCoordinateRegion region = [aMapView region];
	latitudeField.text = [NSString stringWithFormat:@"%f", region.center.latitude];
	longitudeField.text = [NSString stringWithFormat:@"%f", region.center.longitude];
}

@synthesize editingContext;
@synthesize location;
@synthesize isEditing;

@end
