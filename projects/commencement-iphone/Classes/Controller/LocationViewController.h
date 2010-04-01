//
//  LocationViewController.h
//  Events
//
//  Created by Aaron Thompson on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "Location.h"

@interface LocationViewController : UIViewController <MKMapViewDelegate, UITextFieldDelegate> {
	IBOutlet UITextField *nameField;
	IBOutlet UIBarButtonItem *saveButton;
	IBOutlet UIBarButtonItem *editButton;
	IBOutlet UIBarButtonItem *cancelButton;
	IBOutlet MKMapView *mapView;
	
	NSManagedObjectContext *editingContext;
	Location *location;
	BOOL isEditing;
}

- (IBAction)save:(id)sender;
- (IBAction)edit:(id)sender;
- (IBAction)cancel:(id)sender;

@property (retain) NSManagedObjectContext *editingContext;
@property (retain) Location *location;
@property (retain) IBOutlet MKMapView *mapView;
@property (assign) BOOL isEditing;

@end
