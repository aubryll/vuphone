//
//  SectionTitleAndButtonCellController.h
//  Dining
//
//  Created by Aaron Thompson on 1/31/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VUTableViewCellController.h"

@interface SectionTitleAndButtonCellController : NSObject <VUTableViewCellController> {
	NSString *sectionTitle;
	NSString *buttonTitle;
	id buttonTarget;
	SEL buttonSelector;
	id buttonObject;
}

@property (copy) NSString *sectionTitle;
@property (copy) NSString *buttonTitle;
@property (assign) id buttonTarget;
@property (assign) SEL buttonSelector;
@property (assign) id buttonObject;

@end
