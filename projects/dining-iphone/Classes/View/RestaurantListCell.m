//
//  RestaurantListCell.m
//  Dining
//
//  Created by Aaron Thompson on 1/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantListCell.h"


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

- (void)setMinutesUntilClose:(NSNumber *)minutes
{
	int clockMinutes = [minutes intValue] % 60;
	
	if ([minutes intValue] <= 0) {
		self.lowerRightLabel.text = @"closed";
		self.lowerRightLabel.textColor = [UIColor grayColor];
	} else if ([minutes intValue] <= 60) {
		self.lowerRightLabel.text = [NSString stringWithFormat:@"closes in %@m", minutes];
		self.lowerRightLabel.textColor = [UIColor redColor];
	} else {
		int hours = [minutes intValue] / 60;
		self.lowerRightLabel.text = [NSString stringWithFormat:@"closes in %ih %im", hours, clockMinutes];
		self.lowerRightLabel.textColor = [UIColor greenColor];
	}
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)dealloc {
	self.lowerRightLabel = nil;
	
    [super dealloc];
}


@end
