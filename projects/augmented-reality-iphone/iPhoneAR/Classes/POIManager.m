//
//  POIManager.m
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 Gotow.net Creative. All rights reserved.
//

#import "POIManager.h"

static POIManager * sharedPOIManager;

@implementation POIManager

#pragma mark Singleton Implementation

+ (POIManager*)sharedManager
{
    @synchronized(self) {
        if (sharedPOIManager == nil) {
            sharedPOIManager = [[self alloc] init];
        }
    }
    return sharedPOIManager;
}
 
+ (id)allocWithZone:(NSZone *)zone
{
    @synchronized(self) {
        if (sharedPOIManager == nil) {
            sharedPOIManager = [super allocWithZone:zone];
            return sharedPOIManager;
        }
    }
    return nil;
}
 
- (id)copyWithZone:(NSZone *)zone
{
    return self;
}
 
- (id)retain
{
    return self;
}
 
- (unsigned)retainCount
{
    //denotes an object that cannot be released
    return UINT_MAX;
}
 
- (void)release
{
    //do nothing
}
 
- (id)autorelease
{
    return self;
}

- (id)init
{
    if (self = [super init]){
        layers = [[NSMutableArray alloc] init];
        lastCenter = nil;
        lastVisibleQuadrants = CGRectZero;
        visibleRadiusMeters = 1000;
    }
    return self;
}

#pragma mark Requesting POIs

- (CLLocation*)center
{
    return lastCenter;
}

- (void)setCenter:(CLLocation*)location andDesiredPOIRadius:(int)meters
{
    visibleRadiusMeters = meters;
    
    // convert the meters provided into lat/lon distance. 1 degree lat is 60 nautical miles.
    // Each nautical mile is 1852 meters.
    float degrees = ((float)visibleRadiusMeters) / (1852.0 * 60.0);
    
    // determine the rectangle that we want to load points within
    CGRect latLonRect = CGRectMake([location coordinate].latitude - degrees, [location coordinate].longitude - degrees, degrees * 2, degrees * 2);
    
    // convert the gpsRect into quadrants of a fixed size. The conversion between lat/lon and quadrants
    // MUST be fixed between the client and the server to work properly.
    CGRect visibleQuadrants = [self quadrantRectForLatLonRect: latLonRect];
    
    // tell each layer what quadrants are visible. Each layer will post notifications and trigger the
    // display of new points within the visible region.
    for (POILayer * layer in layers)
        [layer setVisibleQuadrants: visibleQuadrants lastVisibleQuadrants: lastVisibleQuadrants];

    // we'll use these next time we update our location!
    [lastCenter release];
    lastCenter = [location retain];
    lastVisibleQuadrants = visibleQuadrants;
}

- (BOOL)isQuadrantWithinVisibleRadius:(POIQuadrant*)q
{
    return (CGRectContainsPoint(lastVisibleQuadrants, CGPointMake(q.x,q.y)));
}

- (BOOL)isLocationWithinVisibleRadius:(CLLocation*)l
{
    // convert the meters provided into lat/lon distance. 1 degree lat is 60 nautical miles.
    // Each nautical mile is 1852 meters.
    float degrees = ((float)visibleRadiusMeters) / (1852.0 * 60.0);
    
    // We don't really care about altitude - just look at 2D lat/lon values.
    CLLocationCoordinate2D center = lastCenter.coordinate;
    CLLocationCoordinate2D current = l.coordinate;
    
    return ((center.latitude-degrees < current.latitude) && (center.latitude+degrees > current.latitude) &&
            (center.longitude-degrees < current.longitude) && (center.longitude+degrees > current.longitude));
}


#pragma mark Transforming Between Coordinate Space and Quadrant Space

- (CGRect)latLonRectForQuadrant:(POIQuadrant*)q
{
    return CGRectMake(q.x / 100.0, q.y / 100.0, 0.01, 0.01);
}

- (CGPoint)quadrantForLatLon:(CGPoint)p
{
    return CGPointMake(floorf(p.x * 100), floorf(p.y * 100));
}

- (CGRect)quadrantRectForLatLonRect:(CGRect)x
{
    x.origin.x = ceilf(x.origin.x * 100);
    x.origin.y = ceilf(x.origin.y * 100);
    x.size.width = floorf(x.size.width * 100);
    x.size.height = floorf(x.size.height * 100);
    return x;
}

#pragma mark Managing POI Cache

- (CGRect)visibleQuadrants
{
    return lastVisibleQuadrants;
}

- (void)saveCache
{
    [NSKeyedArchiver archiveRootObject:layers toFile:@"cachedLayers.archive"];
}

- (void)clearCache
{
    for (POILayer * l in layers)
        [l clearCache];
}

#pragma mark Supplying & Managing Layers

- (NSArray*)layers
{
    return layers;
}

- (void)addLayer:(POILayer*)layer
{
    [layers addObject: layer];
}

@end
