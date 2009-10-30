//
//  VUStartEndDateCellController.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "VUStartEndDateCellController.h"
#import "VUStartEndDatePicker.h"

@implementation VUStartEndDateCellController

- (id)init
{
	self = [super init];
	if (self != nil) {
		isEditable = NO;
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
	if (isEditable)
	{
		// Push a start/end date picker
		if (picker == nil) {
			[[NSBundle mainBundle] loadNibNamed:@"VUStartEndDatePicker" owner:self options:nil];
		}

		picker.startDate = startEndDateCell.startDate;
		picker.endDate = startEndDateCell.endDate;
		UITableViewController *tvc = (UITableViewController *)tableView.dataSource;
		[tvc.navigationController pushViewController:picker animated:YES];
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
	static NSString *identifier = @"startEndDate";
	
	VUStartEndDateCell *cell;
	
	cell = (VUStartEndDateCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		[[NSBundle mainBundle] loadNibNamed:@"VUStartEndDateCell" owner:self options:nil];
		cell = startEndDateCell;
	}
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 88.0f;
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return UITableViewCellEditingStyleNone;
}

- (void)setEditingField:(BOOL)isEditing
{
	isEditable = isEditing;
}

- (void)startDateChanged:(NSDate *)newDate {
	startEndDateCell.startDate = newDate;
}

- (void)endDateChanged:(NSDate *)newDate {
	startEndDateCell.endDate = newDate;
}

- (void)dealloc {
    [super dealloc];
}

@end
