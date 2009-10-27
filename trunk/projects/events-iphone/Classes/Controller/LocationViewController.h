//
//  LocationViewController.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "Location.h"

@interface LocationViewController : UIViewController <MKMapViewDelegate, UITextFieldDelegate> {
	IBOutlet UITextField *nameField;
	IBOutlet UILabel *latitudeLabel;
	IBOutlet UILabel *longitudeLabel;
	IBOutlet UIBarButtonItem *saveButton;
	IBOutlet UIBarButtonItem *editButton;
	IBOutlet MKMapView *mapView;
	
	NSManagedObjectContext *editingContext;
	Location *location;
	BOOL isEditing;
}

- (IBAction)save:(id)sender;
- (IBAction)edit:(id)sender;
- (void)applyIsEditing;

@property (nonatomic, retain) NSManagedObjectContext *editingContext;
@property (nonatomic, retain) Location *location;
@property (assign) BOOL isEditing;

@end
