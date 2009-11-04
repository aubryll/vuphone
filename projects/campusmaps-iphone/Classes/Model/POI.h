//
//  POI.h
//  CampusMaps
//
//  Created by Ben Wibking on 10/16/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>

@class Layer;

@interface POI :  NSManagedObject <MKAnnotation>
{
}

@property (nonatomic, retain) NSNumber * longitude;
@property (nonatomic, retain) NSNumber * latitude;
@property (nonatomic, retain) Layer * layer;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

@end



