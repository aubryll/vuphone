//
//  VUStartEndDateCellController.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "VUStartEndDateCell.h"
#import "VUStartEndDatePicker.h"

@interface VUStartEndDateCellController : UIViewController <VUCellController, VUStartEndDatePickerDelegate, UITextFieldDelegate> {

	IBOutlet VUStartEndDateCell *startEndDateCell;
	IBOutlet VUStartEndDatePicker *picker;

	BOOL isEditable;
}

@end
