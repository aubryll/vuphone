//
//  VUEditableTableViewCell.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "VUEditableCell.h"

@implementation VUEditableCell

- (void)setEditing:(BOOL)editing animated:(BOOL)animated
{
	textField.enabled = editing;
	if (!editing) {
		[textField resignFirstResponder];
	}
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
	if (textField.enabled && selected) {
		[textField becomeFirstResponder];
	}
}

@synthesize textLabel;
@synthesize textField;

@end
