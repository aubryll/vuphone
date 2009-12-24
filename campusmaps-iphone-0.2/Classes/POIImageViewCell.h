//
//  POIImageViewCell.h
//  CampusMaps
//
//  Created by Demetri Miller on 12/24/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface POIImageViewCell : UITableViewCell {
	UIImageView *poiImage;
	
	// Background for the image to be displayed. We need
	// this to be transparent.
	UIView *backView;
}

-(void)setupImage:(UIImage *)image;

-(CGFloat) heightForImage:(UIImageView *) image;
-(CGFloat) widthForImage:(UIImageView *) image;

// Returns the offset needed to evenly space the image horizontally.
-(CGFloat) offsetForImage:(UIImageView *) image;

@property(retain) UIImageView *poiImage;
@property(retain) UIView *backView;
@end
