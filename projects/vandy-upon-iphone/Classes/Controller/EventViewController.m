//
//  EventViewController.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/8/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "EventViewController.h"
#import "VUStartEndDatePicker.h"
#import "VUTableViewController.h"
#import "VUEditableCellController.h"
#import "VUStartEndDateCellController.h"
#import "VULocationCellController.h"

#import "EntityConstants.h"
#import "NSManagedObject-IsNew.h"



@implementation EventViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	dateFormatter = [[NSDateFormatter alloc] init];
	
	// Set up KVC for the Event
	
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
		self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit  target:self action:@selector(beginEditing:)];
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
	[self getValuesFromTableIntoEvent:self.event];
	NSError *error;
	if (![context save:&error]) {
		NSLog(@"There was an error saving the event: %@", error);
		return;
	}
	self.navigationItem.leftBarButtonItem = nil;
	self.navigationItem.rightBarButtonItem = editButton;
	[self endEditingFields];
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

// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSLog(@"cellForRowAtIndexPath %i, %i", indexPath.section, indexPath.row);
	UITableViewCell *cell = [super tableView:aTableView cellForRowAtIndexPath:indexPath];
	
	switch (indexPath.section) {
		case 0:	// Title
			((VUEditableCell *)cell).textField.text = event.name;
			break;
		case 1:	// Start/stop date
			((VUStartEndDateCell *)cell).startDate = event.startTime;
			((VUStartEndDateCell *)cell).endDate = event.endTime;
			break;
		case 2:	// Location
			NSLog(@"Setting event's location to %@", event.location.name);
			cell.detailTextLabel.text = event.location.name;
//			((VULocationCellController *)[[tableGroups objectAtIndex:2] objectAtIndex:0]).location = event.location;
			break;
		case 3:	// Details
			((VUEditableCell *)cell).textField.text = event.details;
			break;
		case 4:	// URL
			((VUEditableCell *)cell).textField.text = event.url;
			break;
	}

	return cell;
}

- (void)constructTableGroups
{
	VUEditableCellController *ecc;
	
	// Name
	ecc = [[VUEditableCellController alloc] initWithLabel:@"Name"];
	ecc.delegate = self;
	NSArray *nameGroup = [NSArray arrayWithObject:[ecc autorelease]];
	
	// Date
	NSArray *dateGroup = [NSArray arrayWithObject:[[[VUStartEndDateCellController alloc] init] autorelease]];

	// Location
	VULocationCellController *locationC = [[VULocationCellController alloc] init];
	locationC.location = self.event.location;
	NSArray *locationGroup = [NSArray arrayWithObject:[locationC autorelease]];

	// Details
	ecc = [[VUEditableCellController alloc] initWithLabel:@"Details"];
	ecc.delegate = self;
	NSArray *detailsGroup = [NSArray arrayWithObject:[ecc autorelease]];
	
	// URL
	ecc = [[VUEditableCellController alloc] initWithLabel:@"URL"];
	ecc.delegate = self;
	NSArray *urlGroup = [NSArray arrayWithObject:[ecc autorelease]];

	tableGroups = [[NSArray arrayWithObjects:nameGroup, dateGroup, locationGroup, detailsGroup, urlGroup, nil] retain];
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (indexPath.section == 1) {
		return 88.0;
	} else {
		return 44.0;
	}
}

- (void)cellControllerValueChanged:(id)newValue {
	NSLog(@"cellControllerValueChanged: %@", newValue);
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
	
	path = [NSIndexPath indexPathForRow:0 inSection:2];
	VULocationCellController *locationC = [[tableGroups objectAtIndex:2] objectAtIndex:0];
	[anEvent setLocation:locationC.location];
	
	// Details
	[anEvent setDetails:[self valueForVUEditableTableViewRow:0 inSection:3]];
	
	// URL
	[anEvent setUrl:[self valueForVUEditableTableViewRow:0 inSection:4]];
}

- (id)valueForVUEditableTableViewRow:(NSUInteger)row inSection:(NSUInteger)section
{
	NSIndexPath *indexPath = [NSIndexPath indexPathForRow:row inSection:section];
	VUEditableCell *cell = (VUEditableCell *)[myTableView cellForRowAtIndexPath:indexPath];
	return cell.textField.text;
}

- (void)setEvent:(Event *)newEvent
{
	if (event != newEvent) {
		[event release];
		event = [newEvent retain];
		[myTableView reloadData];
	}
}

@synthesize event;
@synthesize context;

@end
