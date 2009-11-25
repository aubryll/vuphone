//
//  LocationListViewController.m
//  Events
//
//  Created by Aaron Thompson on 9/20/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LocationListViewController.h"
#import "LocationViewController.h"
#import "LocationCellController.h"
#import "EventViewController.h"
#import "EntityConstants.h"
#import "EventStore.h"

@implementation LocationListViewController

- (void)viewDidLoad {
	self.navigationItem.rightBarButtonItem = addButton;
}

- (void)viewWillAppear:(BOOL)animated {
	// If locations is nil, load all root-level locations
	if (!self.locations) {
		self.locations = [Location rootLocations:[[EventStore sharedEventStore] sharedContext]];
	}
	self.tableView.editing = NO;
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
	[super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
	[addButton release];
}


#pragma mark Table view methods

- (IBAction)addLocation:(id)sender
{
	// Get the shared managed object context
	NSManagedObjectContext *context = [[EventStore sharedEventStore] sharedContext];

	// Create a new location in the shared event store
	Location *location = (Location *)[NSEntityDescription insertNewObjectForEntityForName:VUEntityNameLocation
																   inManagedObjectContext:context];
	location.ownerDeviceId = [[UIDevice currentDevice] uniqueIdentifier];
	location.parentLocation = self.parentLocation;
	if (self.parentLocation != nil) {
		location.latitude = self.parentLocation.latitude;
		location.longitude = self.parentLocation.longitude;
	} else {
		// Default the lat/lng to VU's campus
		location.latitude = [NSDecimalNumber decimalNumberWithString:@"36.146671"];
		location.longitude = [NSDecimalNumber decimalNumberWithString:@"-86.803709"];
	}

	// Push a new location view controller
	LocationViewController *lvc = [[LocationViewController alloc] initWithNibName:@"LocationView" bundle:nil];
	lvc.location = location;
	lvc.editingContext = context;
	lvc.isEditing = YES;
	[self.navigationController pushViewController:lvc animated:YES];
	[lvc release];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return 1;
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [locations count];
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	static NSString *CellIdentifier = @"LocationCell";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
		cell.accessoryType = UITableViewCellAccessoryDetailDisclosureButton;
	}
	
	// Set up the cell
	Location *location = (Location *)[locations objectAtIndex:indexPath.row];
	cell.textLabel.text = ([location.name length] == 0) ? @"(No name)" : location.name;
	
	return cell;
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return UITableViewCellEditingStyleNone;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	// If the location has child locations, push a new copy of this view controller with the selected location as the parent
	// Else, this has no sub-locations, so push the location view controller
	Location *location = (Location *)[locations objectAtIndex:indexPath.row];
	UITableViewController *tvc = (UITableViewController *)tableView.dataSource;

	if ([location.childLocations count] > 0 || self.isEditing) {
		// Push a new LocationListViewController with this location as the parent
		LocationListViewController *llvc = [[LocationListViewController alloc] initWithNibName:@"LocationListView" bundle:nil];
		llvc.locations = [location.childLocations allObjects];
		llvc.parentLocation = location;
		llvc.title = location.name;
		llvc.isEditing = self.isEditing;
		
		[tvc.navigationController pushViewController:llvc animated:YES];
		[llvc release];
	} else {
		// Show the location
		LocationViewController *lvc = [[LocationViewController alloc] initWithNibName:@"LocationView" bundle:nil];
		lvc.location = location;
		lvc.isEditing = self.isEditing;
		if (isEditing) {
			// Give it an editing context
			lvc.editingContext = [[EventStore sharedEventStore] editingContext];
		}

		[tvc.navigationController pushViewController:lvc animated:YES];
		[lvc release];
	}
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)tableView:(UITableView *)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath
{
	// Pop all the way to the EventViewController
	for (NSObject *controller in self.navigationController.viewControllers)
	{
		if ([controller isKindOfClass:[EventViewController class]])
		{
			[[NSNotificationCenter defaultCenter]
				postNotificationName:LocationChosenNotification
							  object:[self.locations objectAtIndex:indexPath.row]];

			[self.navigationController popToViewController:(UIViewController *)controller animated:YES];
			return;
		}
	}
}


- (void)dealloc {
	[super dealloc];
}


@synthesize locations;
@synthesize parentLocation;
@synthesize isEditing;

@end
