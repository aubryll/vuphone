//
//  LocationListViewController.h
//  Events
//
//  Created by Aaron Thompson on 9/20/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Location.h"

@interface LocationListViewController : UITableViewController {
	IBOutlet UIBarButtonItem *addButton;
	NSArray *locations;
	Location *parentLocation;
	
	BOOL isEditing;
}

- (IBAction)addLocation:(id)sender;

@property (nonatomic, retain) NSArray *locations;
@property (nonatomic, retain) Location *parentLocation;
@property (assign) BOOL isEditing;

@end
