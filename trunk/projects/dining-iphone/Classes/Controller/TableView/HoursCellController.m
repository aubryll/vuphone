//
//  HoursCellController.m
//  Dining
//
//  Created by Aaron Thompson on 1/31/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "HoursCellController.h"
#import "OpenHourCell.h"
#import "HourRange.h"

@implementation HoursCellController

@synthesize sectionTitle;
@synthesize hours;

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	return self.sectionTitle;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [hours count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *identifier = @"hccID";
	
	OpenHourCell *cell = (OpenHourCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		cell = [[[OpenHourCell alloc] initWithStyle:UITableViewCellStyleDefault 
										  reuseIdentifier:identifier] autorelease];
	}
	
	HourRange *range = (HourRange *)[self.hours objectAtIndex:indexPath.row];
	cell.dayLabel.text = [range.day capitalizedString];
	cell.hourRangeLabel.text = [range formattedHoursString];
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 30.0f;
}

@end
