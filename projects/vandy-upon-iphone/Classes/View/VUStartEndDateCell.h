//
//  VUStartEndDateTableViewCell.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VUStartEndDateCell : UITableViewCell
{
	IBOutlet UILabel *startTextLabel;
	IBOutlet UILabel *endTextLabel;
	
	NSDate *startDate;
	NSDate *endDate;
	NSDateFormatter *dateFormatter;
}

- (NSDateFormatter *)dateFormatter;

@property (nonatomic, retain, setter=setStartDate) NSDate *startDate;
@property (nonatomic, retain, setter=setEndDate) NSDate *endDate;

@end
