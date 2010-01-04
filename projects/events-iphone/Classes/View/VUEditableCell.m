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
	textField.delegate = self;
	
	[self setEditable:NO];

	return [self retain];
}

- (void)dealloc
{
	[controller release];
	self.textLabel = nil;
	self.textField = nil;
	self.valueView = nil;

	[super dealloc];
}

@synthesize textLabel;
@synthesize textField;
@synthesize valueView;

- (void)setEditable:(BOOL)editable
{
	textField.placeholder = (editable) ? @"(tap to edit)" : nil;

	if (editable) {
		textField.hidden = NO;
		valueView.hidden = YES;
	} else {
		[textField resignFirstResponder];
		textField.hidden = YES;
		
		// Resize the value view to fit its content exactly and show it
		valueView.frame = CGRectMake(valueView.frame.origin.x,
									 valueView.frame.origin.y,
									 valueView.contentSize.width,
									 valueView.contentSize.height);
		
		valueView.hidden = NO;
	}
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
	if (textField.hidden == NO && selected) {
		[textField becomeFirstResponder];
	}

	// Disallow selection if not editable
//	if (textField.hidden) {
		[super setSelected:NO animated:NO];
//	}
}

#pragma mark UITextFieldDelegate

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
	[[NSNotificationCenter defaultCenter]
	 postNotificationName:VUEditableCellBeganEditingNotification object:controller];
}

- (void)textFieldDidEndEditing:(UITextField *)aTextField
{
	[controller textFieldValueChanged:self.textField.text];
	valueView.text = textField.text;
}

- (BOOL)textFieldShouldReturn:(UITextField *)aTextField
{
	[self.textField resignFirstResponder];
	return YES;
}


@end
