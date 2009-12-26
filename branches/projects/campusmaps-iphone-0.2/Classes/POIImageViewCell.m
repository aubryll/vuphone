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
#define kNoImageHeight 86.0f

@implementation POIImageViewCell

@synthesize poiImage;
@synthesize backView;
@synthesize imageLoadingState;
@synthesize loadingIndicator;
@synthesize statusLabel;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {

    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
		// Note that poiImage cannot be initialized until we know what image
		// we are passing it.
		backView = [[UIView alloc] initWithFrame:CGRectZero];
		backView.backgroundColor = [UIColor clearColor];		
		
		imageLoadingState = POIImageIsLoadingState;

		// Set up the loading indicator
		loadingIndicator = [[UIActivityIndicatorView alloc]
							initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];
		CGRect tempFrame = loadingIndicator.frame;
		tempFrame.origin.x = (kCellWidth - tempFrame.size.width) / 2.0f;
		tempFrame.origin.y = 6.0f;
		loadingIndicator.frame = tempFrame;
		loadingIndicator.hidesWhenStopped = YES;
		[loadingIndicator startAnimating];
		[self addSubview:loadingIndicator];
		
		// Set up the status label
		tempFrame.origin.x = 10.0f;
		tempFrame.origin.y += tempFrame.size.height;
		tempFrame.size.width = kCellWidth;
		statusLabel = [[UILabel alloc] initWithFrame:tempFrame];
		statusLabel.textAlignment = UITextAlignmentCenter;
		statusLabel.textColor = [UIColor lightGrayColor];
		statusLabel.text = @"loading image…";
		[self addSubview:statusLabel];
    }
	
    return self;
}


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {

    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setImageLoadingState:(POIImageLoadingState)loadingState
{
	imageLoadingState = loadingState;
	
	switch (loadingState) {
		case POIImageIsLoadingState:
			self.statusLabel.text = @"loading image…";
			[self.loadingIndicator startAnimating];
			break;
		case POIImageFailedToLoadState:
			self.statusLabel.text = @"no image";
			[self.loadingIndicator stopAnimating];
			break;
		case POIImageLoadedState:
			self.statusLabel.text = nil;
			[self.loadingIndicator stopAnimating];
			// Image to be set separately
			break;
		default:
			break;
	}
}

// Initializes our poiImage to the image passed and sets up all the
// properties for how we want it displayed.
- (void)setupImage:(UIImage *)image
{
	poiImage = [[UIImageView alloc] initWithImage:image];
	
	poiImage.frame = CGRectMake([self offsetForImage:poiImage], 
								0.0, 
								[self widthForImage:poiImage], 
								[self heightForImage:poiImage]);
	poiImage.contentMode = UIViewContentModeCenter;
	poiImage.layer.cornerRadius = 10.0f;
	poiImage.clipsToBounds = YES;
	
	[self addSubview:poiImage];
}

- (CGFloat)height
{
	switch (imageLoadingState)
	{
		case POIImageLoadedState:
			return [self heightForImage:poiImage];

		case POIImageIsLoadingState:
		case POIImageFailedToLoadState:
		default:
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
	self.poiImage = nil;
	self.backView = nil;
    [super dealloc];
}


@end
