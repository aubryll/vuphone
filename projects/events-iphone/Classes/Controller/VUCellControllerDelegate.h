/*
 *  VUCellControllerDelegate.h
 *  VandyUpon
 *
 *  Created by Aaron Thompson on 9/25/09.
 *  Copyright 2009 __MyCompanyName__. All rights reserved.
 *
 */
#import "VUCellController.h"

@protocol VUCellControllerDelegate

- (void)cellControllerValueChanged:(id)newValue;

@end
