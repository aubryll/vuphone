//
//  VUEditableTableViewCell.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "VUEditableCell.h"
#import "VUEditableCellController.h"

@implementation VUEditableCell

- (id)initWithController:(VUEditableCellController *)owningController
{
	self = [[[NSBundle mainBundle] loadNibNamed:@"VUEditableTableViewCell" owner:self options:nil] lastObject];
	controller = [owningController retain];
	self.textField.delegate = self;

	return [self retain];
}

- (void)dealloc
{
	[controller release];
	self.textField = nil;
	self.textLabel = nil;
	[super dealloc];
}

@synthesize textLabel;
@synthesize textField;


- (void)setEditable:(BOOL)editable
{
	textField.enabled = editable;
	if (!editable) {
		[textField resignFirstResponder];
	}
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
	if (textField.enabled && selected) {
		[textField becomeFirstResponder];
	}
}

#pragma mark UITextFieldDelegate

- (void)textFieldDidBeginEditing:(UITextField *)aTextField
{
	// Nothing to do
}

- (void)textFieldDidEndEditing:(UITextField *)aTextField
{
	[controller textFieldValueChanged:self.textField.text];
}

- (BOOL)textFieldShouldReturn:(UITextField *)aTextField
{
	[self.textField resignFirstResponder];
	return YES;
}


@end
