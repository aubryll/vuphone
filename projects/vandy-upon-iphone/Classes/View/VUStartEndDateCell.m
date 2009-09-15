//
//  VUStartEndDateTableViewCell.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "VUStartEndDateCell.h"


@implementation VUStartEndDateCell

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
	NSLog(@"VUStartEndDateCell setSelected: %i", selected);
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 88.0;
}

- (void)setStartDate:(NSDate *)date {
	[startDate autorelease];
	// Don't allow a null date
	if (date == nil) {
		date = [NSDate new];
	}
	startDate = [date retain];
	startTextLabel.text = [[self dateFormatter] stringFromDate:startDate];
}

- (void)setEndDate:(NSDate *)date {
	[endDate autorelease];
		
	if (date == nil) {
		date = [NSDate new];
	}
	endDate = [date retain];
	endTextLabel.text = [[self dateFormatter] stringFromDate:endDate];
}

- (NSDateFormatter *)dateFormatter {
	// For whatever reason, I can't get any kind of init method to be called for this class
	if (!dateFormatter) {
		dateFormatter = [[NSDateFormatter alloc] init];
		[dateFormatter setDateFormat:@"EEE, MMM d  hh:mm a"];
	}
	
	return dateFormatter;
}

- (void)dealloc {
	[dateFormatter release];
    [super dealloc];
}


@synthesize startDate;
@synthesize endDate;

@end
