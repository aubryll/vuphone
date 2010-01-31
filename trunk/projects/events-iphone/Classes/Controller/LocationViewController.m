//
//  LocationViewController.m
//  Events
//
//  Created by Aaron Thompson on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LocationCellController.h"
#import "LocationViewController.h"
#import "EventViewController.h"
#import "NSManagedObject-IsNew.h"

@interface LocationViewController(PrivateMethods)

- (void)applyEditing;

@end


@implementation LocationViewController

- (void)viewWillAppear:(BOOL)animated
{
	// Populate the fields from the location
	nameField.text = location.name;
	
	// Set up the map
	[mapView setRegion:MKCoordinateRegionMakeWithDistance(location.coordinate, 200.0, 200.0)];
	[mapView removeAnnotation:location];
	
	if (location) {
		[mapView addAnnotation:location];
	}
	
	[self applyEditing];
}

- (void)viewWillDisappear:(BOOL)animated
{
	if (location) {
		[mapView removeAnnotation:location];
	}
}

- (IBAction)save:(id)sender
{
	location.name = nameField.text;
/*
	// Save changes to the location
	NSError *err;
	BOOL isNew = [location isNew];
	[editingContext save:&err];
*/
	// Pop all the way to the EventViewController
	for (NSObject *controller in self.navigationController.viewControllers) {
		if ([controller isKindOfClass:[EventViewController class]]) {
				[[NSNotificationCenter defaultCenter]
					postNotificationName:LocationChosenNotification
					object:self.location];

			[self.navigationController popToViewController:(UIViewController *)controller animated:YES];
		}
	}
}

- (IBAction)edit:(id)sender
{
	self.isEditing = YES;
	[self applyEditing];
}

- (IBAction)cancel:(id)sender
{	
	BOOL shouldPopView = NO;
	if ([location isNew]) {
		shouldPopView = YES;
	}

	[editingContext rollback];
	
	// Reset the view
	self.isEditing = NO;
	[self applyEditing];
	nameField.text = location.name;
	
	if (shouldPopView) {
		[self.navigationController popViewControllerAnimated:YES];
	}
}

- (void)applyEditing
{
	nameField.enabled = isEditing;
	
	if (isEditing)
	{
		self.title = @"Edit Location";
		self.navigationItem.rightBarButtonItem = saveButton;
		self.navigationItem.leftBarButtonItem = cancelButton;

		nameField.borderStyle = UITextBorderStyleRoundedRect;
		nameField.font = [UIFont boldSystemFontOfSize:12.0f];

		self.mapView.delegate = self;
	}
	else
	{
		self.title = @"Location Details";
		self.navigationItem.leftBarButtonItem = nil;

		// Add or remove the editable button
		if ([location isEditableByDeviceWithId:[[UIDevice currentDevice] uniqueIdentifier]]) {
			self.navigationItem.rightBarButtonItem = editButton;
		} else {
			self.navigationItem.rightBarButtonItem = nil;
		}
		
		nameField.borderStyle = UITextBorderStyleNone;
		nameField.font = [UIFont boldSystemFontOfSize:17.0f];

		self.mapView.delegate = nil;
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
	self.mapView = nil;
}


- (void)dealloc {
    [super dealloc];
}

#pragma mark MKMapViewDelegate

- (void)mapView:(MKMapView *)aMapView regionDidChangeAnimated:(BOOL)animated
{
	MKCoordinateRegion region = [aMapView region];
	
	if (isEditing) {
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
@synthesize mapView;

@end
