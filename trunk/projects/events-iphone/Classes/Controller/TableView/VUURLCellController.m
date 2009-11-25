//
//  VUURLCellController.m
//  Events
//
//  Created by Aaron Thompson on 21/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "VUURLCellController.h"
#import "LocationListViewController.h"
#import "LocationViewController.h"
#import "VUEditableCell.h"

@implementation VUURLCellController

//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (isEditable)
	{
		[super tableView:tableView didSelectRowAtIndexPath:indexPath];
	}
	else
	{
		if (url != nil) {
			[[UIApplication sharedApplication] openURL:url];
		}
	}
}

//
// tableView:cellForRowAtIndexPath:
//
// Returns the cell for a given indexPath.
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	if (cell == nil) {
		cell = [[VUEditableCell alloc] initWithController:self];
	}
	
	cell.textLabel.text = @"URL";
	cell.detailTextLabel.text = [url relativeString];
	
	return cell;
}

- (void)dealloc {
	[url release];
    [super dealloc];
}

@synthesize url;

@end
