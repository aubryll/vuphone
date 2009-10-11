//
//  LocationViewController.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LocationViewController.h"
#import "EventViewController.h"
#import "NSManagedObject-IsNew.h"

@implementation LocationViewController

- (void)viewWillAppear:(BOOL)animated
{
	// Populate the fields from the location
	nameField.text = location.name;
	latitudeField.text = [location.latitude stringValue];
	longitudeField.text = [location.longitude stringValue];
	
	[mapView setRegion:MKCoordinateRegionMakeWithDistance(location.coordinate, 200.0, 200.0)];
	[mapView removeAnnotation:location];
	[mapView addAnnotation:location];
	
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
	BOOL isNew = [location isNew];
	[editingContext save:&err];
	
	// Pop all the way to the EventViewController
	for (NSObject *controller in self.navigationController.viewControllers) {
		if ([controller isKindOfClass:[EventViewController class]]) {
			if (isNew) {
				((EventViewController *)controller).event.location = location;
			}
			[((EventViewController *)controller).tableView reloadData];
			[self.navigationController popToViewController:(UIViewController *)controller animated:YES];
		}
	}
}

- (IBAction)edit:(id)sender
{
	self.isEditing = YES;
	[self applyIsEditing];
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
		self.navigationItem.rightBarButtonItem = editButton;

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

	[mapView removeAnnotation:location];
	[mapView addAnnotation:location];
}

#pragma mark UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
	[textField resignFirstResponder];
	return YES;
}



@synthesize editingContext;
@synthesize location;
@synthesize isEditing;

@end
