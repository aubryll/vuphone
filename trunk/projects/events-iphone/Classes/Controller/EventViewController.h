//
//  EventViewController.h
//  Events
//
//  Created by Aaron Thompson on 9/8/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "Event.h"
#import "Location.h"

#import "VUTableViewController.h"
#import "VUCellControllerDelegate.h"

@interface EventViewController : VUTableViewController <VUCellControllerDelegate>
{
	IBOutlet UITableView *myTableView;
	IBOutlet UIBarButtonItem *saveButton;
	IBOutlet UIBarButtonItem *cancelButton;
	IBOutlet UIBarButtonItem *cancelAddButton;
	IBOutlet UIBarButtonItem *editButton;

	Event *event;
	NSManagedObjectContext *context;
	NSDateFormatter *dateFormatter;
}

- (IBAction)save:(id)sender;
- (IBAction)cancelAdd:(id)sender;
- (IBAction)beginEditing:(id)sender;
- (IBAction)cancelEditing:(id)sender;

@property (nonatomic, retain, setter=setEvent:) Event *event;
@property (nonatomic, retain) NSManagedObjectContext *context;

@end
