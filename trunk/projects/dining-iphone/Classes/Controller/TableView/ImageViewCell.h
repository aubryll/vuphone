//
//  POIImageViewCell.h
//  CampusMaps
//
//  Created by Demetri Miller on 12/24/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ImageViewCell : UITableViewCell {

	UIImageView *mainImageView;
	UILabel *noImageLabel;
}

- (void)setImage:(UIImage *)image;
- (CGFloat)height;
- (CGFloat)heightForImage:(UIImageView *)image;
- (CGFloat)widthForImage:(UIImageView *)image;

// Returns the offset needed to evenly space the image horizontally.
- (CGFloat)offsetForImage:(UIImageView *)image;

@property (retain) UIImageView *mainImageView;
@property (retain) UILabel *noImageLabel;

@end
