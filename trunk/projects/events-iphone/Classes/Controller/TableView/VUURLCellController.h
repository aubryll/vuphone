//
//  VULocationCellController.h
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "EventRating.h"

@interface VUURLCellController : UIViewController <VUCellController> {

	NSURL *url;
}

@property (nonatomic, retain) NSURL *url;

@end
