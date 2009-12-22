//
//  EventListCell.h
//  Events
//
//  Created by Aaron Thompson on 12/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface EventListCell : UITableViewCell {
	UILabel *nameLabel;
	UILabel *timeLabel;
}

- (void)setName:(NSString *)name;
- (void)setTime:(NSDate *)time;

@end
