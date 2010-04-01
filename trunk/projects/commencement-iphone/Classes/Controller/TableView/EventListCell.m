//
//  EventListCell
//  Commencement
//
//  Created by Aaron Thompson on 12/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "EventListCell.h"


@implementation EventListCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
		// Add a shorter name label
		nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(10.0, 3.0, 236.0, 40.0)];
		nameLabel.font = [UIFont boldSystemFontOfSize:[UIFont systemFontSize]+4.0];
		[self addSubview:nameLabel];
		
		// Add a shorter time label
		timeLabel = [[UILabel alloc] initWithFrame:CGRectMake(246.0, 3.0, 54.0, 40.0)];
		timeLabel.font = [UIFont systemFontOfSize:[UIFont systemFontSize]+4.0];
		timeLabel.textColor = [UIColor colorWithRed:56.0/255.0
													 green:84.0/255.0
													  blue:135.0/255.0
													 alpha:1.0];
		[self addSubview:timeLabel];
    }
    return self;
}

- (void)setName:(NSString *)name {
	nameLabel.text = name;
}

- (void)setTime:(NSDate *)time {
	// Set up the date formatter
	static NSDateFormatter *dateFormatter = nil;
	if (dateFormatter == nil)
	{
		dateFormatter = [[NSDateFormatter alloc] init];
		[dateFormatter setDateFormat:@"h:mm"];
	}
	
	timeLabel.text = [dateFormatter stringFromDate:time];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)dealloc {
	[nameLabel release];
	[timeLabel release];
    [super dealloc];
}


@end
