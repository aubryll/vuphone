//
//  VUEditableTableViewCellController.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "VUEditableCellController.h"

@implementation VUEditableCellController

- (id)initWithLabel:(NSString *)aLabel
{
	self = [super init];
	if (self != nil) {
		label = [aLabel retain];
	}
	return self;
}

- (void)dealloc {
	[editableCell release];
	self.delegate = nil;
    [super dealloc];
}

- (UITableViewCell *)cell
{
	if (editableCell == nil) {
		editableCell = [[[VUEditableCell alloc] initWithController:self] retain];
	}
	editableCell.textLabel.text = self.label;
	editableCell.textField.text = self.value;
	[editableCell setEditable:isEditable];
	
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


- (void)textFieldValueChanged:(NSString *)newValue
{
	self.value = newValue;
	if (delegate && [delegate respondsToSelector:@selector(cellControllerValueChanged:forKey:)]) {
		[delegate cellControllerValueChanged:self.value forKey:key];
	}
}

@synthesize label;
@synthesize value;
@synthesize key;
@synthesize delegate;

@end
