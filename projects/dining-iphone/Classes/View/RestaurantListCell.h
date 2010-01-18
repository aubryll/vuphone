//
//  RestaurantListCell.h
//  Dining
//
//  Created by Aaron Thompson on 1/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface RestaurantListCell : UITableViewCell {
	UILabel *lowerRightLabel;
}

@property (retain) UILabel *lowerRightLabel;

- (void)setDistance:(CGFloat)distanceInFeet;
- (void)setMinutesUntilClose:(NSNumber *)minutes;

@end
