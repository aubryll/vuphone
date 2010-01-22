//
//  OpenHourCell.m
//  Dining
//
//  Created by Aaron Thompson on 1/20/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "OpenHourCell.h"

@implementation OpenHourCell

@synthesize dayLabel;
@synthesize hourRangeLabel;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        // Initialization code
		dayLabel = [[UILabel alloc] initWithFrame:CGRectMake(10.0f, 4.0f, 110.0f, 26.0f)];
		dayLabel.backgroundColor = [UIColor clearColor];
		dayLabel.textColor = [UIColor colorWithRed:0.243 green:0.306 blue:0.435 alpha:1.0];
		dayLabel.textAlignment = UITextAlignmentRight;
		dayLabel.font = [UIFont boldSystemFontOfSize:15.0f];
		[self addSubview:dayLabel];
		
		hourRangeLabel = [[UILabel alloc] initWithFrame:CGRectMake(124.0f, 4.0f, 170.0f, 26.0f)];
		hourRangeLabel.backgroundColor = [UIColor clearColor];
		hourRangeLabel.font = [UIFont boldSystemFontOfSize:15.0f];
		[self addSubview:hourRangeLabel];
    }

    return self;
}

- (void)dealloc {
	[dayLabel release];
	[hourRangeLabel release];
    [super dealloc];
}


@end
