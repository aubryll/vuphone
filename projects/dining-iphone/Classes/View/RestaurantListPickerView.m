//
//  RestaurantListPickerView.m
//  Dining
//
//  Created by Aaron Thompson on 1/18/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantListPickerView.h"


@implementation RestaurantListPickerView

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event
{
	NSLog(@"hitTest at %f, %f", point.x, point.y);
	return [super hitTest:point withEvent:event];
}


@end
