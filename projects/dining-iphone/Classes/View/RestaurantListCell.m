//
//  RestaurantListCell.m
//  Dining
//
//  Created by Aaron Thompson on 1/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantListCell.h"
#import "HourRange.h"

@implementation RestaurantListCell

@synthesize lowerRightLabel;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
	{
		self.lowerRightLabel = [[UILabel alloc] initWithFrame:CGRectMake(180.0f, 23.0f, 130.0f, 20.0f)];
		self.lowerRightLabel.textColor = [UIColor darkGrayColor];
		self.lowerRightLabel.textAlignment = UITextAlignmentRight;
		self.lowerRightLabel.font = [UIFont systemFontOfSize:[UIFont systemFontSize] - 1.0f];		
		[self addSubview:self.lowerRightLabel];
    }
	
    return self;
}

- (void)setDistance:(CGFloat)distanceInFeet
{
	if (distanceInFeet < 600.0f) {
		self.lowerRightLabel.text = [NSString stringWithFormat:@"%f feet away", distanceInFeet];
	} else {
		CGFloat distanceInMiles = distanceInFeet / 5280.0f;
		self.lowerRightLabel.text = [NSString stringWithFormat:@"%f miles away", distanceInMiles];
	}
}

- (void)setMinutesUntilClose:(NSInteger)minutes
{
	if (minutes < -60) {
		// Note that we are hard-coding GMT -6 for the date
		int minutesSinceReferenceDate = (int)ceil([NSDate timeIntervalSinceReferenceDate] / 60) - 60*6;
		int minuteOfDayNow = minutesSinceReferenceDate % (24*60);
		int closeMinute = minuteOfDayNow + minutes;
		
		self.lowerRightLabel.text = [NSString stringWithFormat:@"closed, opens at %@",
									 [HourRange dateStringForMinuteOfDay:closeMinute]];
		self.lowerRightLabel.textColor = [UIColor grayColor];
	} else if (minutes <= 0) {
//		self.lowerRightLabel.text = [NSString stringWithFormat:@"closed, opens in %im", minutes];
		self.lowerRightLabel.text = @"closed";
		self.lowerRightLabel.textColor = [UIColor grayColor];
	} else if (minutes <= 60) {
		self.lowerRightLabel.text = [NSString stringWithFormat:@"closes in %im", minutes];
		self.lowerRightLabel.textColor = [UIColor redColor];
	} else if (minutes > 24*60) {	// > 1 day from now, so it's open 24 hrs today
		self.lowerRightLabel.text = @"24 hours";
		self.lowerRightLabel.textColor = [UIColor colorWithRed:0.0f green:0.5f blue:0.0f alpha:1.0f];
	} else {
		int minutesSinceReferenceDate = (int)ceil([NSDate timeIntervalSinceReferenceDate] / 60) - 60*6;
		int minuteOfDayNow = minutesSinceReferenceDate % (24*60);
		int closeMinute = minuteOfDayNow + minutes;
		self.lowerRightLabel.text = [NSString stringWithFormat:@"closes at %@",
									 [HourRange dateStringForMinuteOfDay:closeMinute]];
		self.lowerRightLabel.textColor = [UIColor colorWithRed:0.0f green:0.5f blue:0.0f alpha:1.0f];
	}
}

- (void)dealloc {
	self.lowerRightLabel = nil;
	
    [super dealloc];
}


@end
