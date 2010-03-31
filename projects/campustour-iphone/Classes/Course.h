//
//  Course.h
//  Campus Tour
//
//  Created by sma1 on 3/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Course : NSObject {
	NSString *subject;
	NSString *title;
	NSString *time;
	NSString *place;
	NSString *desc;
}

@property (nonatomic, retain) NSString *subject;
@property (nonatomic, retain) NSString *title;
@property (nonatomic, retain) NSString *time;
@property (nonatomic, retain) NSString *place;
@property (nonatomic, retain) NSString *desc;

@end
