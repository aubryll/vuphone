//
//  MapLayerController.h
//  CampusMaps
//
//  Created by Ben Wibking on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import <MapKit/MKAnnotation.h>
#import "Layer.h"


@interface MapLayerController : NSObject {
	Layer *layer;
	MKMapView *mapView;
	NSSet *filteredPOIs;
}

@property (retain) NSSet *filteredPOIs;

- (id)initWithLayer:(Layer *)aLayer andMapView:(MKMapView *)aMapView;
- (void)addAnnotationsToMapView;
- (void)removeAnnotationsFromMapView;
- (void)setPredicate:(NSPredicate *)pred forContext:(NSManagedObjectContext *)context;
- (void)contextSaved:(NSNotification *)notification;

@end
