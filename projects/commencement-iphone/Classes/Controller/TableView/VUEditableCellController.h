//
//  VUEditableTableViewCellController.h
//  Commencement
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "VUCellControllerDelegate.h"
#import "VUEditableCell.h"

#define VUEditableCellBeganEditingNotification @"VUEditableCellBeganEditingNotification"
#define VUEditableCellEndedEditingNotification @"VUEditableCellEndedEditingNotification"

@interface VUEditableCellController : UIViewController <VUCellController, UITextFieldDelegate> {

	VUEditableCell *editableCell;
	NSString *key;
	id delegate;

	NSString *label;
	NSString *value;
	
	BOOL isEditable;
	
	CGFloat animatedDistance;
}

- (id)initWithLabel:(NSString *)aLabel;
- (void)textFieldValueChanged:(NSString *)newValue;
- (VUEditableCell *)cell;

@property (readonly, copy) NSString *label;
@property (copy) NSString *value;
@property (copy) NSString *key;
@property (retain) id delegate;

@end
