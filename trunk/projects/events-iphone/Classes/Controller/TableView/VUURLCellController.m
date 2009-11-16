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
	// Set up the cell
	static NSString *identifier = @"url";
	
	UITableViewCell *cell;
	
	cell = (VUEditableCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue2 reuseIdentifier:identifier] autorelease];
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
