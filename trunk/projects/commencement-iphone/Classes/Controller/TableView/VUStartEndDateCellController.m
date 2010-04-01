//
//  VUStartEndDateCellController.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
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

	cell.startDate = startDate;
	cell.endDate = endDate;
	
	return cell;
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

		picker.startDate = startDate;
		picker.endDate = endDate;
		UITableViewController *tvc = (UITableViewController *)tableView.dataSource;
		[tvc.navigationController pushViewController:picker animated:YES];
	}
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}
- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return VUStartEndDateCellHeight;
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
	self.startDate = newDate;

	if (delegate && [delegate respondsToSelector:@selector(cellControllerValueChanged:forKey:)]) {
		[delegate cellControllerValueChanged:newDate forKey:startKey];
	}
}

- (void)endDateChanged:(NSDate *)newDate {
	self.endDate = newDate;

	if (delegate && [delegate respondsToSelector:@selector(cellControllerValueChanged:forKey:)]) {
		[delegate cellControllerValueChanged:newDate forKey:endKey];
	}
}

- (void)setStartDate:(NSDate *)start {
	if (start == nil) {
		start = [NSDate date];
	}
	
	if (startDate != start) {
		[startDate release];
		startDate = [start retain];
	}
}

- (void)setEndDate:(NSDate *)end {
	if (end == nil) {
		end = [NSDate dateWithTimeIntervalSinceNow:3600];
	}
	
	if (endDate != end) {
		[endDate release];
		endDate = [end retain];
	}
}

- (void)dealloc {
    [super dealloc];
}

@synthesize startDate;
@synthesize endDate;
@synthesize startKey;
@synthesize endKey;
@synthesize delegate;

@end
