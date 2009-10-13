//
//  VUSourceTableViewCell.h
//  Events
//
//  Created by Aaron Thompson on 10/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface VUSourceTableViewCell : UITableViewCell {
	NSString *source;
	BOOL isChecked;
}

@property (retain, setter=setSource:) NSString *source;
@property (assign, setter=setChecked:) BOOL isChecked;

@end
