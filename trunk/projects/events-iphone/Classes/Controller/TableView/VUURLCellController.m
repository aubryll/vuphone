//
//  VUURLCellController.m
//  Events
//
//  Created by Aaron Thompson on 21/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
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
		if (value != nil) {
			// If it doesn't start with http, add http://
			NSString *URLString = self.value;

			if ([self.value compare:@"http" options:NSCaseInsensitiveSearch range:NSMakeRange(0, 3)] != 0) {
				URLString = [@"http://" stringByAppendingString:self.value];
			}

			[[UIApplication sharedApplication] openURL:[NSURL URLWithString:URLString]];
		}
	}
}

// Overriden in order to set up the text field
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	VUEditableCell *cell = (VUEditableCell *)[super tableView:tableView cellForRowAtIndexPath:indexPath];
	cell.textField.keyboardType = UIKeyboardTypeURL;
	cell.textField.autocorrectionType = UITextAutocorrectionTypeNo;
	cell.textField.autocapitalizationType = UITextAutocapitalizationTypeNone;
	
	return cell;
}

- (void)dealloc {
    [super dealloc];
}

@end
