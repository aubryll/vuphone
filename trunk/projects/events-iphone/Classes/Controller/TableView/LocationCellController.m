//
//  VULocationCellController.m
//  Events
//
//  Created by Aaron Thompson on 21/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "LocationCellController.h"
#import "LocationListViewController.h"
#import "LocationViewController.h"
#import "VUEditableCell.h"

@implementation LocationCellController

- (id) init
{
	self = [super init];
	if (self != nil) {
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(locationChanged:)
													 name:LocationChosenNotification
												   object:nil];
	}
	return self;
}

//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewController *tvc = (UITableViewController *)tableView.dataSource;

	if (isEditable)
	{
		// Push a LocationListViewController
		LocationListViewController *controller = [[LocationListViewController alloc] initWithNibName:@"LocationListView" bundle:nil];
		controller.parentLocation = self.location.parentLocation;
		controller.isEditing = YES;
		controller.title = @"Locations";
		[tvc.navigationController pushViewController:controller animated:YES];
		[controller release];
	}
	else
	{
		// Push a LocationViewController
		LocationViewController *controller = [[LocationViewController alloc] initWithNibName:@"LocationView" bundle:nil];
		controller.location = self.location;
		controller.isEditing = NO;
		[tvc.navigationController pushViewController:controller animated:YES];
		[controller release];
	}

	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

//
// tableView:cellForRowAtIndexPath:
//
// Returns the cell for a given indexPath.
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	// Set up the cell
	static NSString *identifier = @"location";
	
	VUEditableCell *cell;
	
	cell = (VUEditableCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue2 reuseIdentifier:identifier] autorelease];
	}

	cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	cell.textLabel.text = @"Location";
	if (location == nil) {
		cell.detailTextLabel.text = @"unknown";
		cell.detailTextLabel.textColor = [UIColor lightGrayColor];
	} else if (location.name == nil) {
		cell.detailTextLabel.text = @"(tap to choose a location)";
		cell.detailTextLabel.textColor = [UIColor lightGrayColor];
	} else {
		cell.detailTextLabel.text = location.name;
		cell.detailTextLabel.textColor = [UIColor darkTextColor];
	}
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 44.0f;
}

- (void)setEditingField:(BOOL)isEditing
{
	isEditable = isEditing;
}

- (void)locationChanged:(NSNotification *)notification
{
	self.location = [notification object];

	if (delegate && [delegate respondsToSelector:@selector(cellControllerValueChanged:forKey:)]) {
		[delegate cellControllerValueChanged:self.location forKey:key];
	}	
}

- (void)dealloc {
	[location release];
    [super dealloc];
}

@synthesize location;
@synthesize key;
@synthesize delegate;

@end
