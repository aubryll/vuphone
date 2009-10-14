//
//  POIQuadrant.h
//  iPhoneAR
//
//  Created by Ben Gotow on 10/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

@interface POIQuadrant : NSObject <MKAnnotation> {

    int               x;
    int               y;
    NSDate          * cacheDate;
    NSMutableArray  * points;
}

@property (nonatomic, assign) int x;
@property (nonatomic, assign) int y;
@property (nonatomic, retain) NSDate * cacheDate;
@property (nonatomic, retain) NSMutableArray * points;

- (id)initWithX:(int)ax andY:(int)ay;
- (void)dealloc;

- (NSString*)key;
- (NSTimeInterval)age;

- (CLLocationCoordinate2D)coordinate;

@end
