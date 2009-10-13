//
//  VUStartEndDatePicker.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VUStartEndDatePickerDelegate.h"

@interface VUStartEndDatePicker : UITableViewController {

	IBOutlet UIDatePicker *datePicker;
	IBOutlet UITableView *myTableView;
	IBOutlet NSObject<VUStartEndDatePickerDelegate> *delegate;
	
	NSDate *startDate;
	NSDate *endDate;
	NSDateFormatter *dateFormatter;
}

- (IBAction)didChangeDate:(id)sender;

@property (nonatomic, retain, setter=setStartDate:) NSDate *startDate;
@property (nonatomic, retain, setter=setEndDate:) NSDate *endDate;

@end
