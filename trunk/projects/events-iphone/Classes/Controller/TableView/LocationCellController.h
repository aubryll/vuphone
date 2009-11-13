//
//  VULocationCellController.h
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "Location.h"

@interface LocationCellController : UIViewController <VUCellController> {

	Location *location;
	
	BOOL isEditable;
}

@property (nonatomic, retain) Location *location;

@end
