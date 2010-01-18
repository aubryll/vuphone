//
//  RestaurantTypeButton.m
//  Dining
//
//  Created by Aaron Thompson on 1/18/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantTypeButton.h"


@implementation RestaurantTypeButton

- (id)initWithFrame:(CGRect)frame
{
	self = [super initWithFrame:frame];
	if (self != nil) {
		self.bounds = self.frame;
		self.backgroundColor = [UIColor clearColor];
		self.titleLabel.font = [UIFont boldSystemFontOfSize:20.0];
		self.titleLabel.textAlignment = UITextAlignmentLeft;
		[self setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
		[self setTitle:@"Placeholder" forState:UIControlStateNormal];
		self.userInteractionEnabled = YES;
		self.enabled = YES;
		[self addTarget:self action:@selector(debugEvents:) forControlEvents:UIControlEventAllTouchEvents];
	}
	return self;
}

- (UIView *)hitTest:(CGPoint)point withEvent:(UIEvent *)event
{
	NSLog(@"hitTest at %f, %f", point.x, point.y);
	return self;
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
	NSLog(@"touchesBegan withEvent: %@", event);
}

- (void)debugEvents:(id)sender
{
	NSLog(@"debugEvents: %@", sender);
}

- (CGRect)titleRectForContentRect:(CGRect)rect
{
	return rect;
}

- (void)setTitle:(NSString *)title
{
	[self setTitle:title forState:UIControlStateNormal];
	[self setTitle:title forState:UIControlStateSelected];
	[self setTitle:title forState:UIControlStateHighlighted];
}

@end
