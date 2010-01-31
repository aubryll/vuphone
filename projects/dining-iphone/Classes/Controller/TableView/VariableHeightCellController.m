//
//  VariableHeightCellController.m
//  Dining
//
//  Created by Aaron Thompson on 1/31/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "VariableHeightCellController.h"
#import "VariableHeightCell.h"

@implementation VariableHeightCellController

@synthesize sectionTitle;
@synthesize contentStrings;

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	return self.sectionTitle;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [contentStrings count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *identifier = @"vhccID";
	
	VariableHeightCell *cell = (VariableHeightCell *)[tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		cell = [[[VariableHeightCell alloc] initWithStyle:UITableViewCellStyleDefault 
										  reuseIdentifier:identifier] autorelease];
	}
	
	[cell setText:[self.contentStrings objectAtIndex:indexPath.row]];
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	VariableHeightCell *cell = (VariableHeightCell *)[self tableView:aTableView cellForRowAtIndexPath:indexPath];

	return [cell height];
}

@end
