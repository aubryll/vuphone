//
//  VUStartEndDatePicker.m
//  Commencement
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "VUStartEndDatePicker.h"


@implementation VUStartEndDatePicker

- (void)viewDidLoad {
	[super viewDidLoad];

	dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"EEE, MMM d  hh:mm a"];

	self.startDate = [NSDate new];
	self.endDate = [NSDate new];
}

- (void)viewDidAppear:(BOOL)animated {
	// Select the Start field
	[myTableView selectRowAtIndexPath:[NSIndexPath indexPathForRow:0 inSection:0]
							 animated:NO
					   scrollPosition:UITableViewScrollPositionNone];
	datePicker.date = self.startDate;
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
	[super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	self.startDate = nil;
	self.endDate = nil;
}

- (IBAction)didChangeDate:(id)sender {
	NSIndexPath *indexPath = [myTableView indexPathForSelectedRow];
	
	if (indexPath.row == 0) {
		// Start date
		self.startDate = datePicker.date;
	} else {
		// End date
		self.endDate = datePicker.date;
	}
	[myTableView reloadData];
	[myTableView selectRowAtIndexPath:indexPath animated:NO scrollPosition:UITableViewScrollPositionNone];
}

#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return 1;
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return 2;
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	static NSString *CellIdentifier = @"Cell";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier] autorelease];
	}
	
	// Set up the cell
	if (indexPath.row == 0) {	// Start date
		cell.textLabel.text = @"Start";
		cell.detailTextLabel.text = [dateFormatter stringFromDate:startDate];
	} else {	// End date
		cell.textLabel.text = @"End";
		cell.detailTextLabel.text = [dateFormatter stringFromDate:endDate];
	}
	
	return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	datePicker.date = (indexPath.row == 0) ? startDate : endDate;
}


- (void)dealloc {
	[super dealloc];
}

- (void)setStartDate:(NSDate *)newDate {
	[startDate autorelease];
	startDate = [newDate retain];

	// If the new start date is greater than the end date, move the end date
	if ([newDate compare:endDate] == NSOrderedDescending) {
		[self setEndDate:[newDate copy]];
	}
	
	[myTableView reloadData];
	
	// Also notify the delegate
	if (delegate) {
		[delegate startDateChanged:startDate];
	}
}

- (void)setEndDate:(NSDate *)newDate {
	[endDate autorelease];
	endDate = [newDate retain];

	// If the new start date is greater than the end date, move the end date
	if ([newDate compare:startDate] == NSOrderedAscending) {
		[self setStartDate:[newDate copy]];
	}
	
	[myTableView reloadData];

	// Also notify the delegate
	if (delegate) {
		[delegate endDateChanged:endDate];
	}
}

@synthesize startDate;
@synthesize endDate;

@end

