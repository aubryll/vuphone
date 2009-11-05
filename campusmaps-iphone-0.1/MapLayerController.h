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
#import "POI.h"
#import "Layer.h"


@interface MapLayerController : NSObject {
	NSManagedObjectContext* managedObjectContext;
	Layer *layer;
}

- (id)initWithObjectContext:(NSManagedObjectContext *)context;
- (void) addAnnotationsToMapView:(MKMapView*) mapView;

@end
