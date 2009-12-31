//
//  POIQuadrant.m
//  iPhoneAR
//
//  Created by Ben Gotow on 10/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POIQuadrant.h"
#import "POIManager.h"

@implementation POIQuadrant

@synthesize x;
@synthesize y;
@synthesize cacheDate;
@synthesize points;

- (id)initWithX:(int)ax andY:(int)ay
{
    if (self = [super init]){
        x = ax;
        y = ay;
        points = [[NSMutableArray alloc] init];
    }
    return self;
}

- (void)dealloc
{
    [points release];
    [super dealloc];
}

- (NSString*)key
{
    return [NSString stringWithFormat:@"%d,%d", x, y];
}

- (void)resetAge
{
    [cacheDate release];
    cacheDate = [[NSDate date] retain];
}

- (NSTimeInterval)age
{
    if (cacheDate != nil)
        return [[NSDate date] timeIntervalSinceDate: cacheDate];
    else 
        return INFINITY;
}

- (CLLocationCoordinate2D)coordinate
{
    CGRect rect = [[POIManager sharedManager] latLonRectForQuadrant: self];
    CLLocationCoordinate2D p;
    p.latitude = rect.origin.x + rect.size.width / 2;
    p.longitude = rect.origin.y + rect.size.height / 2;
    return p;
}

@end
