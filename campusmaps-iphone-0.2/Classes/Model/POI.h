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

#define BASE_IMAGE_URL_STRING @"http://www.vanderbilt.edu/map/"

enum {
	POIImageIsLoadingState, 
	POIImageLoadedState,
	POIImageFailedToLoadState, 
	POIImageNotYetLoadingState	
};
typedef NSUInteger POIImageLoadingState;


@class Layer;

@interface POI : NSManagedObject <MKAnnotation>
{
	UIImage *_image;
	
	POIImageLoadingState imageLoadingState;
}

@property (nonatomic, retain) NSDecimalNumber * longitude;
@property (nonatomic, retain) NSDecimalNumber * latitude;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) Layer * layer;
@property (nonatomic, retain) NSString * subtitle;
@property (nonatomic, retain) NSString * details;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSString * url;
@property (readonly) POIImageLoadingState imageLoadingState;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

+ (POI *)POIWithServerId:(NSString *)anId inContext:(NSManagedObjectContext *)context;
- (void)setEPSG900913CoordinatesLat:(double)x andLon:(double)y;
- (UIImage *)image;
- (void)loadImage;
- (NSString *)distanceFromLocation:(CLLocation *)location;

@end

