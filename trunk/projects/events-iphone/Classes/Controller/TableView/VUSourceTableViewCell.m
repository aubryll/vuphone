//
//  VUSourceTableViewCell.m
//  Events
//
//  Created by Aaron Thompson on 10/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "VUSourceTableViewCell.h"


@implementation VUSourceTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        // Initialization code
    }
    return self;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


- (void)dealloc {
    [super dealloc];
}

- (void)setSource:(NSString *)newSource {
	NSLog(@"setSource: %@", newSource);
	if (source != newSource) {
		[source release];
		source = [newSource retain];
		self.textLabel.text = source;
	}
}

- (void)setChecked:(BOOL)checked {
	isChecked = checked;

	if (isChecked) {
		self.accessoryType = UITableViewCellAccessoryCheckmark;
	} else {
		self.accessoryType = UITableViewCellAccessoryNone;
	}
}

@synthesize source;
@synthesize isChecked;

@end
