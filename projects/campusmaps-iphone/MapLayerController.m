//
//  MapLayerController.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapLayerController.h"


@implementation MapLayerController

-(id) initWithCoordinate: (CLLocationCoordinate2D) inputCoordinate objectContext: (NSManagedObjectContext*) context {
	if (self = [super init]) {
		managedObjectContext = context;
	
		POI *point = [NSEntityDescription insertNewObjectForEntityForName:@"POI" inManagedObjectContext:managedObjectContext];
		point.latitude = [NSNumber numberWithDouble:inputCoordinate.latitude];
		point.longitude = [NSNumber numberWithDouble:inputCoordinate.longitude];
	
		layer = [NSEntityDescription insertNewObjectForEntityForName:@"Layer" inManagedObjectContext:managedObjectContext];
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
	[super dealloc];
}


@end
