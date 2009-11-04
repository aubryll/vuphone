// 
//  POI.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/16/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POI.h"

#import "Layer.h"

@implementation POI 

@dynamic longitude;
@dynamic latitude;
@dynamic layer;

-(CLLocationCoordinate2D)coordinate {
	CLLocationCoordinate2D coord;
	
	//[self willAccessValueForKey:@"longitude"];
	coord.longitude = [self.longitude doubleValue];
	//[self didAccessValueForKey:@"longitude"];
	
	//[self willAccessValueForKey:@"latitude"];
	coord.latitude = [self.latitude doubleValue];
	//[self didAccessValueForKey:@"latitude"];
	
	return coord;
}

@end
