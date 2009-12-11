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
}

- (id)initWithLayer:(Layer *)aLayer;
- (void)addAnnotationsToMapView:(MKMapView *)mapView;
- (void)removeAnnotationsFromMapView:(MKMapView *)mapView;

@end
