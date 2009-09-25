// 
//  Event.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "Event.h"
#import "Location.h"

@implementation Event 

@dynamic ownerAndroidId;
@dynamic url;
@dynamic name;
@dynamic startTime;
@dynamic details;
@dynamic endTime;
@dynamic location;

- (NSString *)title {
	return self.name;
}

- (NSString *)subtitle {
	return self.location.name;
}

- (CLLocationCoordinate2D)coordinate {
	CLLocationCoordinate2D coords;
	coords.latitude = [self.location.latitude doubleValue];
	coords.longitude = [self.location.longitude doubleValue];
	return coords;
}

@end
