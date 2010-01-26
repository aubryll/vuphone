//
//  RestaurantTypePickerView.h
//  Dining
//
//  Created by Aaron Thompson on 1/18/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#define RestaurantTypePickerViewTag 41

@interface RestaurantTypePickerView : UIView {
	BOOL isChecked;
}

@property (assign, setter=setIsChecked:) BOOL isChecked;

- (id)initWithFrame:(CGRect)frame buttonTag:(NSInteger)buttonTag buttonTarget:(id)buttonTarget buttonAction:(SEL)buttonAction;

@end
