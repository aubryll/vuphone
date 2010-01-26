//
//  RestaurantTypePickerView.m
//  Dining
//
//  Created by Aaron Thompson on 1/18/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantTypePickerView.h"


@implementation RestaurantTypePickerView

@synthesize isChecked;

- (void)setIsChecked:(BOOL)checked
{
	isChecked = checked;
}

- (id)initWithFrame:(CGRect)frame buttonTag:(NSInteger)buttonTag buttonTarget:(id)buttonTarget buttonAction:(SEL)buttonAction
{
    if (self = [super initWithFrame:frame])
	{
		self.tag = RestaurantTypePickerViewTag;
		
		self.backgroundColor = [UIColor clearColor];
		self.userInteractionEnabled = YES;		
    }
    return self;
}


- (void)dealloc {
    [super dealloc];
}


@end
