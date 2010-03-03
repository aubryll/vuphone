//
//  Waypoint.m
//  Campus Tour
//
//  Created by sma1 on 2/9/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "Waypoint.h"


@implementation Waypoint

@synthesize name;
@synthesize description;
@synthesize audioFilePath;
@synthesize num;
@synthesize image;
@synthesize coordinate;

- (id)init
{
	[super init];
	name = @"Unknown Location";
	description = @"Blah Blah Blah..";
	audioFilePath = @"";
	coordinate.latitude = 36.142;
	coordinate.longitude =  -86.8044;
	num = 0;
	image = 0;
	return self;
}


- (NSString *)title
{
	return name;
}

- (NSString *)subtitle
{
	return description;
}

-(void)setLocation:(double)longitude latitude:(double)latitude
{
	coordinate.longitude = longitude;
	coordinate.latitude = latitude;
}

@end
