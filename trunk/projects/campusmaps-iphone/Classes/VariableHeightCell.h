//
//  VariableHeightCell.h
//  CampusMaps
//
//  Created by Demetri Miller on 12/23/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface VariableHeightCell : UITableViewCell {
	UITextView *textView;
}

- (void)setText:(NSString *)text;
- (CGFloat)height;

@property(retain) UITextView *textView;

@end
