//
//  VULocationCellController.m
//  Events
//
//  Created by Aaron Thompson on 21/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "LocationCellController.h"
#import "LocationListViewController.h"
#import "LocationViewController.h"
#import "VUEditableCell.h"

@implementation LocationCellController

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
	
	UITableViewCell *cell;
	
	cell = (VUEditableCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue2 reuseIdentifier:identifier] autorelease];
	}

	cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	cell.textLabel.text = @"Location";
	cell.detailTextLabel.text = location.name;
	
	return cell;
}

- (void)setEditingField:(BOOL)isEditing
{
	isEditable = isEditing;
}

- (void)dealloc {
	[location release];
    [super dealloc];
}

@synthesize location;

@end
