//
//  VUEventListCell.m
//  Events
//
//  Created by Aaron Thompson on 10/29/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "VUEventListCell.h"


@implementation VUEventListCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        // Initialization code
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated
{
	CGRect frame = self.textLabel.frame;
	frame.size.width -= 30.0f;
	self.textLabel.frame = frame;
}	

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)dealloc {
    [super dealloc];
}


@end
