//
//  MapLayerController.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapLayerController.h"


@implementation MapLayerController

- (MapLayerController*)initWithCoordinate:(CLLocationCoordinate2D)inputCoordinate {
	[super init];
	
	coordinate = inputCoordinate;
	return self;
}


- (void) dealloc {
	[super dealloc];
}

@synthesize coordinate;


@end
