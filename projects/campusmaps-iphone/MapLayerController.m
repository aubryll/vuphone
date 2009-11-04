//
//  MapLayerController.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapLayerController.h"


@implementation MapLayerController

- (id)initWithObjectContext:(NSManagedObjectContext *)context
{
	if (self = [super init]) {
		managedObjectContext = [context retain];

		POI *point = (POI *)[NSEntityDescription insertNewObjectForEntityForName:@"POI" inManagedObjectContext:managedObjectContext];
		point.latitude = [NSNumber numberWithDouble:CAMPUS_CENTER_LATITUDE];
		point.longitude = [NSNumber numberWithDouble:CAMPUS_CENTER_LONGITUDE];
	
		layer = (Layer *)[NSEntityDescription insertNewObjectForEntityForName:@"Layer" inManagedObjectContext:managedObjectContext];
		[layer addPOIsObject:point];
	}

	return self;
}

- (void) addAnnotationsToMapView:(MKMapView*) mapView {
	NSEnumerator *enumerator = [layer.POIs objectEnumerator];
	POI* point;
	
	while (point = [enumerator nextObject]) {
		[mapView addAnnotation:point];
	}
}

- (void) dealloc {
	[managedObjectContext release];
	[super dealloc];
}


@end
