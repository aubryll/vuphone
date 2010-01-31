//
//  ImageCellController.h
//  Dining
//
//  Created by Aaron Thompson on 1/31/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VUTableViewCellController.h"

@interface ImageCellController : NSObject <VUTableViewCellController> {
	UIImage *image;
}

@property (retain) UIImage *image;

@end
