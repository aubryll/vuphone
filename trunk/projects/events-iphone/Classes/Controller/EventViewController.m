//
//  EventViewController.m
//  Events
//
//  Created by Aaron Thompson on 9/8/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
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

	if (isEditingFields)
	{
		if ([event isNew]) {
			self.navigationItem.leftBarButtonItem = cancelAddButton;
		} else {
			self.navigationItem.leftBarButtonItem = cancelButton;
		}
		self.navigationItem.rightBarButtonItem = saveButton;
	}
	else
	{
		// If the user may edit this event
		if ([event isEditableByDeviceWithId:[[UIDevice currentDevice] uniqueIdentifier]]) {
			self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit  target:self action:@selector(beginEditing:)];
		}
	}
}

- (void)viewDidAppear:(BOOL)animated
{
	if (isEditingFields) {
		[self beginEditingFields];
	} else {
		[self endEditingFields];
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
	
	[self getValuesFromTableIntoEvent:self.event];

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
	self.navigationItem.leftBarButtonItem = cancelButton;
	self.navigationItem.rightBarButtonItem = saveButton;
}

- (IBAction)cancelEditing:(id)sender
{
	[self endEditingFields];
	self.navigationItem.leftBarButtonItem = nil;
	self.navigationItem.rightBarButtonItem = editButton;
	[context rollback];
	[myTableView reloadData];
}


#pragma mark Table view methods

- (void)constructTableGroups
{
	VUEditableCellController *ecc;
	// Name
	ecc = [[VUEditableCellController alloc] initWithLabel:@"Name"];
	ecc.delegate = self;
	ecc.value = event.name;
	NSArray *nameGroup = [NSArray arrayWithObject:[ecc autorelease]];
	
	// Date
	VUStartEndDateCellController *sedcc = [[[VUStartEndDateCellController alloc] init] autorelease];
	sedcc.startDate = event.startTime;
	sedcc.endDate = event.endTime;
	
	NSArray *dateGroup = [NSArray arrayWithObject:sedcc];

	// Location
	LocationCellController *locationC = [[LocationCellController alloc] init];
	locationC.location = self.event.location;
	NSArray *locationGroup = [NSArray arrayWithObject:[locationC autorelease]];

	// Details
	ecc = [[VUEditableCellController alloc] initWithLabel:@"Details"];
	ecc.delegate = self;
	ecc.value = event.details;
	NSArray *detailsGroup = [NSArray arrayWithObject:[ecc autorelease]];
	
	// Ratings
	EventRatingsCellController *ercc = [[EventRatingsCellController alloc] init];
	ercc.ratings = event.ratings;
	NSArray *ratingsGroup = [NSArray arrayWithObject:[ercc autorelease]];
	
	// URL
	ecc = [[VUURLCellController alloc] initWithLabel:@"URL"];
	ecc.delegate = self;
	ecc.value = event.url;
	NSArray *urlGroup = [NSArray arrayWithObject:[ecc autorelease]];

	tableGroups = [[NSArray arrayWithObjects:nameGroup, dateGroup, locationGroup, detailsGroup, ratingsGroup, urlGroup, nil] retain];
}

- (void)cellControllerValueChanged:(id)newValue {
	[self getValuesFromTableIntoEvent:self.event];
}

- (void)getValuesFromTableIntoEvent:(Event *)anEvent
{
	// Name
	[anEvent setName:[self valueForVUEditableTableViewRow:0 inSection:0]];
	
	// Start/stop time
	NSIndexPath *path = [NSIndexPath indexPathForRow:0 inSection:1];
	VUStartEndDateCell *startEndCell = (VUStartEndDateCell *)[self.tableView cellForRowAtIndexPath:path];
	[anEvent setStartTime:startEndCell.startDate];
	[anEvent setEndTime:startEndCell.endDate];
	
	// Details
	[anEvent setDetails:[self valueForVUEditableTableViewRow:0 inSection:3]];
	
	// URL
	[anEvent setUrl:[self valueForVUEditableTableViewRow:0 inSection:5]];
}

- (id)valueForVUEditableTableViewRow:(NSUInteger)row inSection:(NSUInteger)section
{
	VUEditableCellController *controller = (VUEditableCellController *)[[tableGroups objectAtIndex:section] objectAtIndex:row];
	return controller.value;
}

- (void)setEvent:(Event *)newEvent
{
	if (event != newEvent) {
		[event release];
		event = [newEvent retain];

		[self updateAndReload];
	}
}

@synthesize event;
@synthesize context;

@end
