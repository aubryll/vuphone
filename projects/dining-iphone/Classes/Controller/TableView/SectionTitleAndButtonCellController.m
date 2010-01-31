//
//  SectionTitleAndButtonCellController.m
//  Dining
//
//  Created by Aaron Thompson on 1/31/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "SectionTitleAndButtonCellController.h"

@implementation SectionTitleAndButtonCellController

@synthesize sectionTitle;
@synthesize buttonTitle;
@synthesize buttonTarget;
@synthesize buttonSelector;
@synthesize buttonObject;

#pragma mark VUTableViewCellController

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	return self.sectionTitle;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	// Don't show the button if it has no title
	return (buttonTitle == nil) ? 0 : 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *identifier = @"stabccID";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue2
									 reuseIdentifier:identifier] autorelease];
	}

	cell.detailTextLabel.text = buttonTitle;
	
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	if (self.buttonTarget != nil) {
		[self.buttonTarget performSelectorOnMainThread:buttonSelector
											withObject:buttonObject
										 waitUntilDone:NO];
	} else {
		[tableView deselectRowAtIndexPath:indexPath animated:NO];
	}
}

@end
