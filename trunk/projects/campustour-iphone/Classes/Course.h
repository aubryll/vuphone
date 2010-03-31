//
//  Course.h
//  Campus Tour
//
//  Created by sma1 on 3/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Course : NSObject {
	NSString *number;
	NSString *title;
	NSString *time;
	NSString *place;
}

@property (retain) NSString *number;
@property (retain) NSString *title;
@property (retain) NSString *time;
@property (retain) NSString *place;

@end
