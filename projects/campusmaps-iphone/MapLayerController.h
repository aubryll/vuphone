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


@interface MapLayerController : NSObject <MKAnnotation> {
	CLLocationCoordinate2D coordinate;
}

- (id)initWithCoordinate:(CLLocationCoordinate2D) inputCoordinate;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

@end
