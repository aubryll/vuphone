//
//  GenericTableViewController.m
//  PhoneNumbers
//
//  Created by Matt Gallagher on 27/12/08.
//  Copyright 2008 Matt Gallagher. All rights reserved.
//
//  Permission is given to use this source code file without charge in any
//  project, commercial or otherwise, entirely at your risk, with the condition
//  that any redistribution (in part or whole) of source code must retain
//  this copyright and permission notice. Attribution in compiled projects is
//  appreciated but not required.
//

#import "VUTableViewController.h"
#import "VUCellController.h"

@interface VUTableViewController (private)

- (void)propagateEditingFields;
- (void)constructTableGroups;

@end


@implementation VUTableViewController

- (NSArray *)tableGroups
{
	if (!tableGroups)
	{
		[self constructTableGroups];
	}
	
	return tableGroups;
}

//
// constructTableGroups
//
// Creates/updates cell data. This method should only be invoked directly if
// a "reloadData" needs to be avoided. Otherwise, updateAndReload should be used.
//
- (void)constructTableGroups
{
	tableGroups = [[NSArray arrayWithObject:[NSArray array]] retain];
}

//
// clearTableGroups
//
// Releases the table group data (it will be recreated when next needed)
//
- (void)clearTableGroups
{
	[tableGroups release];
	tableGroups = nil;
}

//
// updateAndReload
//
// Performs all work needed to refresh the data and the associated display
//
- (void)updateAndReload
{
	[self clearTableGroups];
	[self constructTableGroups];
	[self.tableView reloadData];
}

//
// numberOfSectionsInTableView:
//
// Return the number of sections for the table.
//
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	return [[self tableGroups] count];
}

//
// tableView:numberOfRowsInSection:
//
// Returns the number of rows in a given section.
//
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return [[[self tableGroups] objectAtIndex:section] count];
}

//
// tableView:cellForRowAtIndexPath:
//
// Returns the cell for a given indexPath.
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return
		[[[[self tableGroups] objectAtIndex:indexPath.section] objectAtIndex:indexPath.row]
			tableView:(UITableView *)tableView
			cellForRowAtIndexPath:indexPath];
}

//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSObject<VUCellController> *cellData =
		[[tableGroups objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
	if ([cellData respondsToSelector:@selector(tableView:didSelectRowAtIndexPath:)])
	{
		[cellData tableView:tableView didSelectRowAtIndexPath:indexPath];
	}
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSObject<VUCellController> *cell = [[tableGroups objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
	if ([cell respondsToSelector:@selector(tableView:editingStyleForRowAtIndexPath:)])
	{
		return [cell tableView:tableView editingStyleForRowAtIndexPath:indexPath];
	}
	
	return UITableViewCellEditingStyleNone;
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSObject<VUCellController> *cell = [[tableGroups objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
	if ([cell respondsToSelector:@selector(tableView:heightForRowAtIndexPath:)])
	{
		return [cell tableView:aTableView heightForRowAtIndexPath:indexPath];
	}
	
	return 44.0f;
}



- (void)beginEditingFields {
	isEditingFields = YES;
	[self propagateEditingFields];
}
- (void)endEditingFields {
	isEditingFields = NO;
	[self propagateEditingFields];
}

- (void)propagateEditingFields
{
	for (NSArray *group in [self tableGroups]) {
		for (NSObject<VUCellController> *cell in group) {
			if ([cell respondsToSelector:@selector(setEditingField:)])
			{
				[cell setEditingField:isEditingFields];
			}
		}
	}
}


//
// didReceiveMemoryWarning
//
// Release any cache data.
//
- (void)didReceiveMemoryWarning
{
	[super didReceiveMemoryWarning];
	
	[self clearTableGroups];
}
//
// dealloc
//
// Release instance memory
//
- (void)dealloc
{
	[self clearTableGroups];
	[super dealloc];
}

@end

