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
	
	[[NSNotificationCenter defaultCenter]
	 addObserver:self
	 selector:@selector(keyboardDidShow:)
	 name:UIKeyboardDidShowNotification
	 object:nil];
	
	[[NSNotificationCenter defaultCenter]
	 addObserver:self
	 selector:@selector(keyboardDidHide:)
	 name:UIKeyboardWillHideNotification
	 object:nil];
	
	[[NSNotificationCenter defaultCenter]
	 addObserver:self
		selector:@selector(editableCellBeganEditing:)
			name:VUEditableCellBeganEditingNotification
		  object:nil];
	
	self.currentCellIndexPath = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	
	if ([event isNew])
	{
		self.navigationItem.leftBarButtonItem = cancelButton;
		self.navigationItem.rightBarButtonItem = saveButton;
	}
	// If the user may edit this event
	else if ([event isEditableByDeviceWithId:[[UIDevice currentDevice] uniqueIdentifier]])
	{
		self.navigationItem.rightBarButtonItem =
		[[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemEdit
													  target:self
													  action:@selector(beginEditing:)];
	}
	else
	{
		self.navigationItem.rightBarButtonItem = nil;
	}	

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
	[dateFormatter release];
	self.currentCellIndexPath = nil;
}


- (void)dealloc {
	[super dealloc];
}

#pragma mark Actions

- (IBAction)save:(id)sender
{
	// Check the input
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

	self.navigationItem.leftBarButtonItem = nil;
	self.navigationItem.rightBarButtonItem = editButton;
	[self endEditingFields];
	[self.tableView reloadData];
	
	self.navigationItem.title = event.title;
	
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
	self.navigationItem.rightBarButtonItem = nil;
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
	// If we are canceling a new event
	if ([event isNew]) {
		[self cancelAdd:sender];
		return;
	}

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
/*	
	// Ratings – don't show if editing
	if (!isEditingFields) {
		EventRatingsCellController *ercc = [[EventRatingsCellController alloc] init];
		ercc.ratings = event.ratings;
		[mTableGroups addObject:[NSArray arrayWithObject:[ercc autorelease]]];
	}
*/	
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


- (void)keyboardDidShow:(NSNotification *)notification
{
	// Shorten the table view's frame by the keyboard's height
	if (!keyboardIsShowing) {
		keyboardIsShowing = YES;
		
		CGRect keyboardBounds;
		[[notification.userInfo valueForKey:UIKeyboardBoundsUserInfoKey] getValue:&keyboardBounds];

		CGRect frame = self.tableView.frame;
		frame.size.height -= (keyboardBounds.size.height - kTabBarHeight);
		self.tableView.frame = frame;
	}

	if (self.currentCellIndexPath) {
		[self.tableView scrollToRowAtIndexPath:self.currentCellIndexPath
							  atScrollPosition:UITableViewScrollPositionNone
									  animated:YES];
	}
}

- (void)keyboardDidHide:(NSNotification *)notification
{
	// Resize the tableview back down
	CGRect keyboardBounds;
	[[notification.userInfo valueForKey:UIKeyboardBoundsUserInfoKey] getValue:&keyboardBounds];

	CGRect frame = self.tableView.frame;
	frame.size.height += (keyboardBounds.size.height - kTabBarHeight);
	self.tableView.frame = frame;

	keyboardIsShowing = NO;
	self.currentCellIndexPath = nil;
}

- (void)editableCellBeganEditing:(NSNotification *)notification
{
	VUEditableCellController *ecc = [notification object];
	
	// Determine the appropriate textField
	for (int i = 0; i<[[self tableGroups] count]; i++) {
		VUTableViewController *tvc = (VUTableViewController *)[[[self tableGroups] objectAtIndex:i] objectAtIndex:0];
		NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:i];

		if ((VUEditableCellController *)tvc == ecc) {
			// Save the scroll path
			self.currentCellIndexPath = indexPath;

//			NSLog(@"editableCellBeganEditing scrolling to section %i", self.currentCellIndexPath.section);
			[self.tableView scrollToRowAtIndexPath:self.currentCellIndexPath
								  atScrollPosition:UITableViewScrollPositionNone
										  animated:YES];
			break;
		}
	}
	
}

- (void)setEvent:(Event *)newEvent
{
	if (event != newEvent) {
		[event release];
		event = [newEvent retain];

		[self clearTableGroups];
	}
}

@synthesize event;
@synthesize context;
@synthesize currentCellIndexPath;

@end
