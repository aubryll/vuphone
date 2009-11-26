//
//  EventViewController.m
//  Events
//
//  Created by Aaron Thompson on 9/8/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "EventViewController.h"
#import "VUStartEndDatePicker.h"
#import "VUTableViewController.h"
#import "VUEditableCellController.h"
#import "VUURLCellController.h"
#import "VUStartEndDateCellController.h"
#import "LocationCellController.h"
#import "EventRatingsCellController.h"
#import "RemoteEventLoader.h"
#import "EntityConstants.h"
#import "NSManagedObject-IsNew.h"



@implementation EventViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	dateFormatter = [[NSDateFormatter alloc] init];
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];

	[self.tableView reloadData];
}


- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
	self.event = nil;
	self.context = nil;
}


- (void)dealloc {
    [super dealloc];
}

#pragma mark Actions

- (IBAction)save:(id)sender
{
	self.navigationItem.leftBarButtonItem = nil;
	self.navigationItem.rightBarButtonItem = editButton;
	[self endEditingFields];
	[self.tableView reloadData];
	
	if (event.location == nil)
	{
		// If the location is not set, show an alert message and bail
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"You must choose a location"
															message:nil
														   delegate:nil
												  cancelButtonTitle:@"OK"
												  otherButtonTitles:nil];
		[alertView show];
		[alertView release];
		return;
	}

	// If this is a new event, set the device ID
	if ([event isNew]) {
		event.ownerDeviceId = [[UIDevice currentDevice] uniqueIdentifier];
	}

	// Save the event to the persistent store
	NSError *error;
	if (![context save:&error]) {
		NSLog(@"There was an error saving the event: %@", error);
		return;
	}

	// Save the event to the server
	[RemoteEventLoader submitEvent:self.event];
	
	self.navigationItem.title = event.name;
}

- (IBAction)cancelAdd:(id)sender
{
	[context rollback];
	self.navigationItem.leftBarButtonItem = nil;
	[self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)beginEditing:(id)sender
{
	[self beginEditingFields];

	[self.tableView reloadData];

	self.navigationItem.leftBarButtonItem = cancelButton;
	self.navigationItem.rightBarButtonItem = saveButton;
}

- (IBAction)cancelEditing:(id)sender
{
	[self endEditingFields];
	
	[context rollback];
	
	[myTableView reloadData];
	
	self.navigationItem.leftBarButtonItem = nil;
	self.navigationItem.rightBarButtonItem = editButton;
}


#pragma mark Table view methods

- (void)constructTableGroups
{
	if (tableGroups) {
		[tableGroups release];
		tableGroups = nil;
	}

	NSMutableArray *mTableGroups = [[NSMutableArray alloc] init];
	
	VUEditableCellController *ecc;

	// Name
	ecc = [[VUEditableCellController alloc] initWithLabel:@"Name"];
	ecc.key = @"name";
	ecc.delegate = self;
	ecc.value = event.name;
	[mTableGroups addObject:[NSArray arrayWithObject:[ecc autorelease]]];
	
	// Date
	VUStartEndDateCellController *sedcc = [[[VUStartEndDateCellController alloc] init] autorelease];
	sedcc.startKey = @"startTime";
	sedcc.endKey = @"endTime";
	sedcc.delegate = self;
	sedcc.startDate = event.startTime;
	sedcc.endDate = event.endTime;
	[mTableGroups addObject:[NSArray arrayWithObject:sedcc]];

	// Location
	LocationCellController *locationC = [[LocationCellController alloc] init];
	locationC.key = @"location";
	locationC.delegate = self;
	locationC.location = self.event.location;
	[mTableGroups addObject:[NSArray arrayWithObject:[locationC autorelease]]];

	// Details
	ecc = [[VUEditableCellController alloc] initWithLabel:@"Details"];
	ecc.key = @"details";
	ecc.delegate = self;
	ecc.value = event.details;
	[mTableGroups addObject:[NSArray arrayWithObject:[ecc autorelease]]];
	
	// Ratings – don't show if editing
	if (!isEditingFields) {
		EventRatingsCellController *ercc = [[EventRatingsCellController alloc] init];
		ercc.ratings = event.ratings;
		[mTableGroups addObject:[NSArray arrayWithObject:[ercc autorelease]]];
	}
	
	// URL – show if not editing or URL is set
	if (event.url != nil || isEditingFields) {
		ecc = [[VUURLCellController alloc] initWithLabel:@"URL"];
		ecc.key = @"url";
		ecc.delegate = self;
		ecc.value = event.url;
		[mTableGroups addObject:[NSArray arrayWithObject:[ecc autorelease]]];
	}
	
	tableGroups = mTableGroups;
}


- (void)cellControllerValueChanged:(id)newValue forKey:(NSString *)key
{
	[self.event setValue:newValue forKey:key];
	[self.tableView reloadData];
}


- (void)setEvent:(Event *)newEvent
{
	if (event != newEvent) {
		[event release];
		event = [newEvent retain];

		[self clearTableGroups];

		// If the user may edit this event
		if ([event isEditableByDeviceWithId:[[UIDevice currentDevice] uniqueIdentifier]]) {
			self.navigationItem.rightBarButtonItem =
			[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit
														  target:self
														  action:@selector(beginEditing:)];
		} else {
			self.navigationItem.rightBarButtonItem = nil;
		}
	}
}

@synthesize event;
@synthesize context;

@end
