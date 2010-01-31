//
//  VariableHeightCellController.h
//  Dining
//
//  Created by Aaron Thompson on 1/31/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VUTableViewCellController.h"

@interface VariableHeightCellController : NSObject <VUTableViewCellController> {
	NSString *sectionTitle;
	NSArray *contentStrings;
}

@property (copy) NSString *sectionTitle;
@property (retain) NSArray *contentStrings;

@end
