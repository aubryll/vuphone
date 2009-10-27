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
	latitudeLabel.text = [location.latitude stringValue];
	longitudeLabel.text = [location.longitude stringValue];
	
	[mapView setRegion:MKCoordinateRegionMakeWithDistance(location.coordinate, 200.0, 200.0)];
	[mapView removeAnnotation:location];
	[mapView addAnnotation:location];
	
	[self applyIsEditing];
}

- (IBAction)save:(id)sender
{
	location.name = nameField.text;

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
	
	if (isEditing) {
		self.title = @"Edit Location";
		self.navigationItem.rightBarButtonItem = saveButton;

		nameField.borderStyle = UITextBorderStyleRoundedRect;
		nameField.font = [UIFont boldSystemFontOfSize:12.0f];
	} else {
		self.title = @"Location Details";
		self.navigationItem.rightBarButtonItem = editButton;

		nameField.borderStyle = UITextBorderStyleNone;
		nameField.font = [UIFont boldSystemFontOfSize:17.0f];
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
	
	if (isEditing) {
		latitudeLabel.text = [NSString stringWithFormat:@"%f", region.center.latitude];
		longitudeLabel.text = [NSString stringWithFormat:@"%f", region.center.longitude];

		[mapView removeAnnotation:location];

		location.latitude = [NSDecimalNumber decimalNumberWithDecimal:
							 [[NSNumber numberWithFloat:region.center.latitude] decimalValue]];
		location.longitude = [NSDecimalNumber decimalNumberWithDecimal:
							  [[NSNumber numberWithFloat:region.center.longitude] decimalValue]];

		[mapView addAnnotation:location];
	}
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
