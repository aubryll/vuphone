//
//  VUEditableTableViewCellController.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "VUCellControllerDelegate.h"
#import "VUEditableCell.h"

@interface VUEditableCellController : UIViewController <VUCellController, UITextFieldDelegate> {

	IBOutlet VUEditableCell *editableTVC;
	id delegate;

	NSString *label;
	NSString *value;
	
	BOOL isEditable;
}

- (id)initWithLabel:(NSString *)aLabel;

@property (readonly, copy) NSString *label;
@property (nonatomic, copy) NSString *value;
@property (nonatomic, retain) id delegate;

@end
