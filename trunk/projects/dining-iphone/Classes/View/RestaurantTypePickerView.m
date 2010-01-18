//
//  RestaurantTypePickerView.m
//  Dining
//
//  Created by Aaron Thompson on 1/18/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantTypePickerView.h"


@implementation RestaurantTypePickerView

@synthesize nameButton;
@synthesize checkmarkLabel;
@synthesize isChecked;

- (void)setIsChecked:(BOOL)checked
{
	isChecked = checked;
	checkmarkLabel.text = (isChecked) ? @"√" : nil;
}

- (id)initWithFrame:(CGRect)frame buttonTag:(NSInteger)buttonTag buttonTarget:(id)buttonTarget buttonAction:(SEL)buttonAction
{
    if (self = [super initWithFrame:frame])
	{
		self.tag = RestaurantTypePickerViewTag;
		
		self.backgroundColor = [UIColor clearColor];
		self.userInteractionEnabled = YES;
		
		// Set up the name button
		self.nameButton = [[RestaurantTypeButton alloc] initWithFrame:CGRectMake(4.0f, 0.0f, frame.size.width - 8.0f, frame.size.height)];
		nameButton.tag = buttonTag;
		[nameButton addTarget:buttonTarget action:buttonAction forControlEvents:UIControlEventTouchUpInside];
		
		[self addSubview:nameButton];
		[nameButton release];

		// Set up the checkmark label
		self.checkmarkLabel = [[UILabel alloc] initWithFrame:CGRectMake(nameButton.frame.size.width, 0.0f, 30.0f, frame.size.height)];
		checkmarkLabel.backgroundColor = [UIColor clearColor];
		checkmarkLabel.font = [UIFont boldSystemFontOfSize:20.0];
		checkmarkLabel.userInteractionEnabled = NO;
		
//		[self addSubview:checkmarkLabel];
		[checkmarkLabel release];
    }
    return self;
}


- (void)dealloc {
	self.nameButton = nil;
	self.checkmarkLabel = nil;
	
    [super dealloc];
}


@end
