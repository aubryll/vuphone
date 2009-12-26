//
//  POIImageViewCell.h
//  CampusMaps
//
//  Created by Demetri Miller on 12/24/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "POI.h"

@interface POIImageViewCell : UITableViewCell {

	POIImageLoadingState imageLoadingState;
	
	UIImageView *poiImage;
	UIActivityIndicatorView *loadingIndicator;
	UILabel *statusLabel;
	
	// Background for the image to be displayed. We need
	// this to be transparent.
	UIView *backView;
}

- (void)setupImage:(UIImage *)image;
- (CGFloat)height;
- (CGFloat)heightForImage:(UIImageView *)image;
- (CGFloat)widthForImage:(UIImageView *)image;

// Returns the offset needed to evenly space the image horizontally.
- (CGFloat)offsetForImage:(UIImageView *)image;

@property (retain) UIImageView *poiImage;
@property (retain) UIView *backView;
@property (assign, setter=setImageLoadingState) POIImageLoadingState imageLoadingState;
@property (retain) UIActivityIndicatorView *loadingIndicator;
@property (retain) UILabel *statusLabel;

@end
