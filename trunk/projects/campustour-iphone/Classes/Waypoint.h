//
//  Waypoint.h
//  Campus Tour
//
//  Created by sma1 on 2/9/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Waypoint : NSObject {
	
@private
	NSString *name;
	NSString *description;
	NSString *audioFilePath;
	double latitude;
	double longitude;
	int num;
	UIImage *image;
}

@property (retain) NSString *name;
@property (assign) double latitude;
@property (assign) double longitude;
@property (retain) NSString *description;
@property (retain) NSString *audioFilePath;
@property (assign) int num;
@property (assign) UIImage *image;

@end
