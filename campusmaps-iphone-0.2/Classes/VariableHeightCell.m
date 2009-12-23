//
//  VariableHeightCell.m
//  CampusMaps
//
//  Created by Demetri Miller on 12/23/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "VariableHeightCell.h"


@implementation VariableHeightCell

@synthesize textView;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        // Initialization code
		textView = [[UITextView alloc] initWithFrame:CGRectMake(16.0, 2.0, 280, 40.0)];
		textView.editable = NO;
		textView.font = [UIFont systemFontOfSize:13.0];
		[self addSubview:textView];
    }
    return self;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)setText:(NSString *)text {
	self.textView.text = text;
	CGRect contentRect = self.textView.frame;
	contentRect.size.height = self.textView.contentSize.height;
	self.textView.frame = contentRect;
}

- (CGFloat)height {
	return self.textView.frame.size.height + 4.0;
}

- (void)dealloc {
	self.textView = nil;
    [super dealloc];
}


@end
