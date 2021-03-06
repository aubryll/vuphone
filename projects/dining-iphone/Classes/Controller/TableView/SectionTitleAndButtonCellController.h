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

@property (nonatomic, copy) NSString *sectionTitle;
@property (nonatomic, copy) NSString *buttonTitle;
@property (nonatomic, assign) id buttonTarget;
@property (nonatomic, assign) SEL buttonSelector;
@property (nonatomic, retain) id buttonObject;

@end
