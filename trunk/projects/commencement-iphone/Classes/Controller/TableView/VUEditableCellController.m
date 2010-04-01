//
//  VUEditableTableViewCellController.m
//  Commencement
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "VUEditableCellController.h"

#define DefaultRowHeight 44.0f
#define KeyboardAnimationDuration 1.0f

@implementation VUEditableCellController

- (id)initWithLabel:(NSString *)aLabel
{
	self = [super init];
	if (self != nil) {
		label = [aLabel retain];
		animatedDistance = 0.0f;
	}
	return self;
}

- (void)dealloc {
	[editableCell release];
	self.delegate = nil;
	[super dealloc];
}

- (VUEditableCell *)cell
{
	if (editableCell == nil) {
		editableCell = [[[VUEditableCell alloc] initWithController:self] retain];

		editableCell.textLabel.text = self.label;
		editableCell.textField.text = self.value;
		editableCell.valueView.text = self.value;
		
		[editableCell setEditable:isEditable];
	}
	
	return editableCell;
}

//
// tableView:cellForRowAtIndexPath:
//
// Returns the cell for a given indexPath.
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return [self cell];
}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	VUEditableCell *cellAtRow = (VUEditableCell *)[self cell];

	if (isEditable) {
		return DefaultRowHeight;
	} else {
		if (self.value) {
			return cellAtRow.valueView.frame.size.height;
		} else {
			return DefaultRowHeight;
		}
	}
}

//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[(VUEditableCell *)[self cell] setEditing:isEditable];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}


- (void)setEditingField:(BOOL)isEditing
{
	isEditable = isEditing;
	[(VUEditableCell *)[self cell] setEditable:isEditing];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[self textFieldValueChanged:editableCell.textField.text];
}

/*
- (void)textFieldDidBeginEditing:(UITextField *)textField
{
	CGRect textFieldRect = [self.view.window convertRect:textField.bounds fromView:textField];
	CGRect viewRect = [self.view.window convertRect:self.view.bounds fromView:self.view];
	NSLog(@"textFieldRect: %@", textFieldRect);
	NSLog(@"viewRect: %@", viewRect);
	
	CGRect viewFrame = self.view.frame;
	viewFrame.origin.y -= animatedDistance;
	
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationBeginsFromCurrentState:YES];
	[UIView setAnimationDuration:KeyboardAnimationDuration];
	
	[self.view setFrame:viewFrame];
	
	[UIView commitAnimations];
}

- (void)textFieldDidEndEditing:(UITextField *)textField
{
	CGRect viewFrame = self.view.frame;
	viewFrame.origin.y += animatedDistance;
	
	[UIView beginAnimations:nil context:NULL];
	[UIView setAnimationBeginsFromCurrentState:YES];
	[UIView setAnimationDuration:KeyboardAnimationDuration];
	
	[self.view setFrame:viewFrame];
	
	[UIView commitAnimations];
}
*/
- (void)textFieldValueChanged:(NSString *)newValue
{
	self.value = newValue;
	if (delegate && [delegate respondsToSelector:@selector(cellControllerValueChanged:forKey:)]) {
		[delegate cellControllerValueChanged:self.value forKey:key];
	}
}

- (void)setValue:(NSString *)newValue {
	[value release];
	value = [newValue copy];
}

@synthesize label;
@synthesize value;
@synthesize key;
@synthesize delegate;

@end
