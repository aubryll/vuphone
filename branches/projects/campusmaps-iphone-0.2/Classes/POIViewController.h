//
//  POIViewController.h
//  CampusMaps
//
//  Created by Demetri Miller on 12/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "POI.h"

@interface POIViewController : UIViewController <UITableViewDelegate, UITableViewDataSource> {
	POI *poi;
	CGFloat detailsTextHeight;
}

-(CGFloat) getImageHeight:(UIImageView *) image;
-(CGFloat) getImageWidth:(UIImageView *) image;

// Returns the offset needed to evenly space the image
// horizontally.
-(CGFloat) getImageOffset:(UIImageView *) image;

@property (retain) POI *poi;

@end
