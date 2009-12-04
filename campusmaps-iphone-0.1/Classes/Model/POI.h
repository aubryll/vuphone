//
//  POI.h
//  CampusMaps
//
//  Created by Ben Wibking on 10/16/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>

#define ENTITY_NAME_POI @"POI"

#define CAMPUS_CENTER_LATITUDE 36.142
#define CAMPUS_CENTER_LONGITUDE -86.8044

@class Layer;

@interface POI :  NSManagedObject <MKAnnotation>
{
}

@property (nonatomic, retain) NSDecimalNumber * longitude;
@property (nonatomic, retain) NSDecimalNumber * latitude;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) Layer * layer;
@property (nonatomic, retain) NSString * subtitle;
@property (nonatomic, retain) NSString * details;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSString * url;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

+ (POI *)POIWithServerId:(NSString *)anId inContext:(NSManagedObjectContext *)context;
- (void)setEPSG900913CoordinatesLat:(double)x andLon:(double)y;

@end

