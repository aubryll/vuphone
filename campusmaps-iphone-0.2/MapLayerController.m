//
//  MapLayerController.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "NSManagedObjectContext-Convenience.h"
#import "MapLayerController.h"
#import "POI.h"

@implementation MapLayerController

- (id)initWithLayer:(Layer *)aLayer
{
	if (self = [super init]) {
		layer = [aLayer retain];
		filteredPOIs = nil;
	}

	return self;
}

- (void)addAnnotationsToMapView:(MKMapView *)mapView {
	NSEnumerator *enumerator = (self.filteredPOIs) ? [self.filteredPOIs objectEnumerator] : [layer.POIs objectEnumerator];
	POI* point;
	
	while (point = [enumerator nextObject]) {
		[mapView addAnnotation:point];
	}
}

- (void)removeAnnotationsFromMapView:(MKMapView *)mapView {
	NSEnumerator *enumerator = (self.filteredPOIs) ? [self.filteredPOIs objectEnumerator] : [layer.POIs objectEnumerator];
	POI* point;
	
	while (point = [enumerator nextObject]) {
		[mapView removeAnnotation:point];
	}
}

- (void)setPredicate:(NSPredicate *)pred forContext:(NSManagedObjectContext *)context onMapView:(MKMapView *)mapView
{
	[self removeAnnotationsFromMapView:mapView];
	
	if (pred) {
		NSPredicate *layerPredicate = [NSPredicate predicateWithFormat:@"layer = %@", layer];
		NSPredicate *andPredicate = [NSCompoundPredicate andPredicateWithSubpredicates:
											 [NSArray arrayWithObjects:pred, layerPredicate, nil]];
		self.filteredPOIs = [context fetchObjectsForEntityName:ENTITY_NAME_POI
												 withPredicate:andPredicate];
	} else {
		self.filteredPOIs = nil;
	}
	
	[self addAnnotationsToMapView:mapView];
}

- (void)dealloc {
	[layer release];
	[super dealloc];
}

@synthesize filteredPOIs;

@end
