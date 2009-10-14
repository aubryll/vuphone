//
//  POIManager.h
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 Gotow.net Creative. All rights reserved.
//
/*

The POIManager is responsible for maintaining a collection of cached points of interest
and requesting new points from POIProviders when existing POIs are not available. The
POIManager is a singleton and should multiple instances should never be necessary. The
class is entirely thread-safe and returns POIs via asynchronous notifications.

*/

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "POIQuadrant.h"
#import "POILayer.h"
#import "POIProvider.h"
#import "POI.h"

@interface POIManager : NSObject {

    CLLocation          * lastCenter;
    CGRect                lastVisibleQuadrants;
    int                   visibleRadiusMeters;
    
    NSMutableArray      * layers;
}

#pragma mark Singleton Implementation

+ (POIManager*)sharedManager;
+ (id)allocWithZone:(NSZone *)zone;
- (id)copyWithZone:(NSZone *)zone;
- (id)retain;
- (unsigned)retainCount;
- (void)release;
- (id)autorelease;

#pragma mark Requesting POIs

- (CLLocation*)center;
- (void)setCenter:(CLLocation*)location andDesiredPOIRadius:(int)meters;
- (BOOL)isQuadrantWithinVisibleRadius:(POIQuadrant*)q;
- (BOOL)isLocationWithinVisibleRadius:(CLLocation*)l;

#pragma mark Transforming Between Coordinate Space and Quadrant Space

- (CGRect)latLonRectForQuadrant:(POIQuadrant*)q;
- (CGPoint)quadrantForLatLon:(CGPoint)p;
- (CGRect)quadrantRectForLatLonRect:(CGRect)x;

#pragma mark Managing POI Cache

- (void)saveCache;
- (void)clearCache;

#pragma mark Supplying & Managing Layers

- (void)addLayer:(POILayer*)layer;

@end
