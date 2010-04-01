//
//  VUEditableTableViewCell.h
//  Commencement
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>

@class VUEditableCellController;


@interface VUEditableCell : UITableViewCell <UITextFieldDelegate> {

	IBOutlet UILabel *textLabel;
	IBOutlet UITextField *textField;
	IBOutlet UITextView *valueView;
	
	VUEditableCellController *controller;
}

- (id)initWithController:(VUEditableCellController *)owningController;
- (void)setEditable:(BOOL)editable;

@property (nonatomic, retain) IBOutlet UILabel *textLabel;
@property (retain) IBOutlet UITextField *textField;
@property (retain) IBOutlet UITextView *valueView;

@end
