//
//  VULocationCellController.h
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "VUCellController.h"
#import "VUCellControllerDelegate.h"
#import "Location.h"

#define LocationChosenNotification @"locationChosen"

@interface LocationCellController : UIViewController <VUCellController> {

	Location *location;
	
	BOOL isEditable;
	
	NSString *key;
	id delegate;
}

@property (retain) Location *location;
@property (copy) NSString *key;
@property (retain) id delegate;

@end
