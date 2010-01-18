//
//  RestaurantTypePickerView.h
//  Dining
//
//  Created by Aaron Thompson on 1/18/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RestaurantTypeButton.h"

#define RestaurantTypePickerViewTag 41

@interface RestaurantTypePickerView : UIView {
	RestaurantTypeButton *nameButton;
	UILabel *checkmarkLabel;
	BOOL isChecked;
}

@property (retain) RestaurantTypeButton *nameButton;
@property (retain) UILabel *checkmarkLabel;
@property (assign, setter=setIsChecked:) BOOL isChecked;

- (id)initWithFrame:(CGRect)frame buttonTag:(NSInteger)buttonTag buttonTarget:(id)buttonTarget buttonAction:(SEL)buttonAction;

@end
