//
//  VUStartEndDateCellController.h
//  Commencement
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "VUStartEndDateCell.h"
#import "VUStartEndDatePicker.h"
#import "VUCellControllerDelegate.h"

@interface VUStartEndDateCellController : UIViewController <VUCellController, VUStartEndDatePickerDelegate, UITextFieldDelegate> {

	IBOutlet VUStartEndDateCell *startEndDateCell;
	IBOutlet VUStartEndDatePicker *picker;

	BOOL isEditable;
	NSDate *startDate;
	NSDate *endDate;
	NSString *startKey;
	NSString *endKey;
	id delegate;
}

@property (retain, setter=setStartDate:) NSDate *startDate;
@property (retain, setter=setEndDate:) NSDate *endDate;
@property (copy) NSString *startKey;
@property (copy) NSString *endKey;
@property (retain) id delegate;

@end
