//
//  MapLayerController.m
//  CampusMaps
//
//  Created by Aaron Thompson on 1/19/10.
//  Copyright 2010 Aaron Thompson. All rights reserved.
//

#import "MapLayerController.h"
#import "POI.h"
#import "NSManagedObjectContext-Convenience.h"

@implementation MapLayerController

@synthesize filteredPOIs;
@synthesize savedPredicate;

- (id)initWithLayer:(Layer *)aLayer andMapView:(MKMapView *)aMapView
{
	if (self = [super init]) {
		layer = [aLayer retain];
		mapView = [aMapView retain];
		filteredPOIs = nil;
		
		// Register for notification to update annotations when the MOC is changed
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(contextSaved:)
													 name:NSManagedObjectContextDidSaveNotification
												   object:nil];
	}
	
	return self;
}

- (void)addAnnotationsToMapView {
	NSArray *annotations = (self.filteredPOIs) ? [self.filteredPOIs allObjects] : [layer.POIs allObjects];
	
	[mapView addAnnotations:annotations];
}

- (void)removeAnnotationsFromMapView {
	NSArray *annotations = (self.filteredPOIs) ? [self.filteredPOIs allObjects] : [layer.POIs allObjects];
	
	[mapView removeAnnotations:annotations];
}

- (void)setPredicate:(NSPredicate *)pred forContext:(NSManagedObjectContext *)context
{
	if (pred == savedPredicate) {
		return;
	}
	
	[self removeAnnotationsFromMapView];
	
	if (pred) {
		NSPredicate *layerPredicate = [NSPredicate predicateWithFormat:@"layer = %@", layer];
		NSPredicate *andPredicate = [NSCompoundPredicate andPredicateWithSubpredicates:
									 [NSArray arrayWithObjects:pred, layerPredicate, nil]];
		self.savedPredicate = andPredicate;
		
		self.filteredPOIs = [context fetchObjectsForEntityName:ENTITY_NAME_POI
												 withPredicate:andPredicate];
	} else {
		self.filteredPOIs = nil;
		self.savedPredicate = nil;
	}
	
	[self addAnnotationsToMapView];
}

- (void)contextSaved:(NSNotification *)notification
{
	// Handle inserted objects
	NSSet *insertedObjects = [[notification userInfo] valueForKey:NSInsertedObjectsKey];
	for (NSManagedObject *obj in insertedObjects)
	{
		if ([[[obj entity] name] isEqualToString:ENTITY_NAME_POI]) {
			if (((POI *)obj).layer == layer) {
				// This POI was inserted into this layer
				[mapView addAnnotation:(id<MKAnnotation>)obj];
			}
		}
	}
	
	// Handle updated objects
	NSSet *updatedObjects = [[notification userInfo] valueForKey:NSUpdatedObjectsKey];
	for (NSManagedObject *obj in updatedObjects)
	{
		if ([[[obj entity] name] isEqualToString:ENTITY_NAME_POI]) {
			if (((POI *)obj).layer == layer) {
				// This POI was updated in this layer
				[mapView removeAnnotation:(id<MKAnnotation>)obj];
				[mapView addAnnotation:(id<MKAnnotation>)obj];
			}
		}
	}
	
	// Handle deleted objects
	NSSet *deletedObjects = [[notification userInfo] valueForKey:NSDeletedObjectsKey];
	for (NSManagedObject *obj in deletedObjects)
	{
		if ([[[obj entity] name] isEqualToString:ENTITY_NAME_POI]) {
			if (((POI *)obj).layer == layer) {
				// This POI was deleted from this layer
				[mapView removeAnnotation:(id<MKAnnotation>)obj];
			}
		}
	}
}

- (void)dealloc {
	[layer release];
	[mapView release];
	self.filteredPOIs = nil;
	self.savedPredicate = nil;
	[super dealloc];
}

@end
