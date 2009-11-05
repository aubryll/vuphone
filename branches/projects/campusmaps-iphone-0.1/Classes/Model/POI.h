//
//  POI.h
//  CampusMaps
//
//  Created by Ben Wibking on 10/16/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>

#define CAMPUS_CENTER_LATITUDE 36.146671
#define CAMPUS_CENTER_LONGITUDE -86.803709

@class Layer;

@interface POI :  NSManagedObject <MKAnnotation>
{
}

@property (nonatomic, retain) NSDecimalNumber * longitude;
@property (nonatomic, retain) NSDecimalNumber * latitude;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) Layer * layer;

//@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

@end



