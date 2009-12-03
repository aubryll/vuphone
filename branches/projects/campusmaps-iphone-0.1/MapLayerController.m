//
//  MapLayerController.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapLayerController.h"
#import "POI.h"

@implementation MapLayerController

- (id)initWithLayer:(Layer *)aLayer
{
	if (self = [super init]) {
		layer = [aLayer retain];
	}

	return self;
}

- (void)addAnnotationsToMapView:(MKMapView *)mapView {
	NSEnumerator *enumerator = [layer.POIs objectEnumerator];
	POI* point;
	
	while (point = [enumerator nextObject]) {
		[mapView addAnnotation:point];
	}
}

- (void)removeAnnotationsFromMapView:(MKMapView *)mapView {
	NSEnumerator *enumerator = [layer.POIs objectEnumerator];
	POI* point;
	
	while (point = [enumerator nextObject]) {
		[mapView removeAnnotation:point];
	}
}

- (void)dealloc {
	[layer release];
	[super dealloc];
}


@end
