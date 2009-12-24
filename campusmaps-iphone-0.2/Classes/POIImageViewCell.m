//
//  POIImageViewCell.m
//  CampusMaps
//
//  Created by Demetri Miller on 12/24/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POIImageViewCell.h"
#import <QuartzCore/QuartzCore.h>

#define kCellWidth 300.0f

@implementation POIImageViewCell

@synthesize poiImage;
@synthesize backView;


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        // Initialization code.
		
		// Note that poiImage cannot be initialized until we know what image
		// we are passing it.
		backView = [[UIView alloc] initWithFrame:CGRectZero];
		backView.backgroundColor = [UIColor clearColor];
		
		self.backgroundView = backView;
    }
    return self;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

// Initializes our poiImage to the image passed and sets up all the
// properties for how we want it displayed.
-(void)setupImage:(UIImage *)image
{
	poiImage = [[UIImageView alloc] initWithImage:image];
	
	poiImage.frame = CGRectMake([self getImageOffset:poiImage], 
								0.0, 
								[self getImageWidth:poiImage], 
								[self getImageHeight:poiImage]);
	poiImage.contentMode = UIViewContentModeCenter;
	poiImage.layer.cornerRadius = 10.0f;
	poiImage.clipsToBounds = YES;
	
	[self addSubview:poiImage];
}


- (CGFloat)getImageHeight:(UIImageView *)image 
{
	return image.frame.size.height;
}

- (CGFloat)getImageWidth:(UIImageView *)image
{
	if (image.frame.size.width > kCellWidth) {
		return kCellWidth;
	} else {
		return image.frame.size.width;
	}
}

// Returns the offset needed to evenly space the image
// horizontally.
-(CGFloat) getImageOffset:(UIImageView *) image
{
	return ((kCellWidth - [self getImageWidth:image])/2.0f);
}

- (void)dealloc {
	self.poiImage = nil;
	self.backView = nil;
    [super dealloc];
}


@end
