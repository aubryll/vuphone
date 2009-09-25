/*
 *  VUStartEndDatePickerDelegate.h
 *  VandyUpon
 *
 *  Created by Aaron Thompson on 9/13/09.
 *  Copyright 2009 __MyCompanyName__. All rights reserved.
 *
 */

@protocol VUStartEndDatePickerDelegate

- (void)startDateChanged:(NSDate *)date;
- (void)endDateChanged:(NSDate *)date;

@end
