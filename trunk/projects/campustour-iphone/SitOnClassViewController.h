//
//  SitOnClassViewController.h
//  Campus Tour
//
//  Created by sma1 on 3/23/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Course.h"

@interface SitOnClassViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	NSArray *courses;
}

-(void)loadData:(NSString *)fileName;

@end
