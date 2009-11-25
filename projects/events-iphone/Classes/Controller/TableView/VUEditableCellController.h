//
//  VUEditableTableViewCellController.h
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "VUCellControllerDelegate.h"
#import "VUEditableCell.h"

@interface VUEditableCellController : UIViewController <VUCellController, UITextFieldDelegate> {

	VUEditableCell *cell;
	NSString *key;
	id delegate;

	NSString *label;
	NSString *value;
	
	BOOL isEditable;
}

- (id)initWithLabel:(NSString *)aLabel;
- (void)textFieldValueChanged:(NSString *)newValue;

@property (readonly, copy) NSString *label;
@property (copy) NSString *value;
@property (copy) NSString *key;
@property (retain) id delegate;

@end
