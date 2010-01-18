//
//  ImageViewCell.m
//  Dining
//
//  Adapted from class created by Demetri Miller on 12/24/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "ImageViewCell.h"
#import <QuartzCore/QuartzCore.h>

#define kCellWidth 300.0f
#define kNoImageHeight 46.0f

@implementation ImageViewCell

@synthesize mainImageView;
@synthesize noImageLabel;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
	{
		// Note that imageView cannot be initialized until we know what image
		// we are passing it.
		self.backgroundView = [[UIView alloc] initWithFrame:CGRectZero];
		self.backgroundView.backgroundColor = [UIColor clearColor];
		
		// Set up the no image label
		CGRect tempFrame;
		tempFrame.origin.x = 10.0f;
		tempFrame.origin.y = 6.0f;
		tempFrame.size.width = kCellWidth;
		tempFrame.size.height = kNoImageHeight;

		noImageLabel = [[UILabel alloc] initWithFrame:tempFrame];
		noImageLabel.textAlignment = UITextAlignmentCenter;
		noImageLabel.textColor = [UIColor grayColor];
		noImageLabel.backgroundColor = [UIColor clearColor];
		noImageLabel.text = @"no image";
    }
	
    return self;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setImage:(UIImage *)image
{
	if (image == nil)
	{
		[mainImageView removeFromSuperview];
		[self addSubview:noImageLabel];
	}
	else
	{
		mainImageView = [[UIImageView alloc] initWithImage:image];
		
		mainImageView.frame = CGRectMake([self offsetForImage:mainImageView], 
									0.0, 
									[self widthForImage:mainImageView], 
									[self heightForImage:mainImageView]);
		mainImageView.contentMode = UIViewContentModeCenter;
		mainImageView.layer.cornerRadius = 10.0f;
		mainImageView.clipsToBounds = YES;
		
		[noImageLabel removeFromSuperview];
		[self addSubview:mainImageView];
	}
}

- (CGFloat)height
{
	if (mainImageView.image) {
		return [self heightForImage:mainImageView];
	} else {
		return kNoImageHeight;
	}
}

- (CGFloat)heightForImage:(UIImageView *)image 
{
	return image.frame.size.height ;
}

- (CGFloat)widthForImage:(UIImageView *)image
{
	if (image.frame.size.width > kCellWidth) {
		return kCellWidth;
	} else {
		return image.frame.size.width;
	}
}

// Returns the offset needed to evenly space the image
// horizontally.
- (CGFloat)offsetForImage:(UIImageView *)image
{
	return (((kCellWidth - [self widthForImage:image])/2.0f) + 10.0f);
}

- (void)dealloc {
	self.mainImageView = nil;
    [super dealloc];
}


@end
