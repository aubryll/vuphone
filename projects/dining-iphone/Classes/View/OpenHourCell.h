//
//  OpenHourCell.h
//  Dining
//
//  Created by Aaron Thompson on 1/20/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface OpenHourCell : UITableViewCell {
	UILabel *dayLabel;
	UILabel *hourRangeLabel;
}

@property (readonly) UILabel *dayLabel;
@property (readonly) UILabel *hourRangeLabel;

@end
