//
//  POI.m
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 Gotow.net Creative. All rights reserved.
//

#import "POI.h"


@implementation POI

@synthesize title;
@synthesize details;
@synthesize location;

- (id)initWithLocation:(CLLocation*)l
{
    if (self = [super init]){
        location = [l retain];
        title = nil;
        details = nil;
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{    
    if (self = [super init]){
        self.location = [aDecoder decodeObject];
        self.details = [aDecoder decodeObject];
        self.title = [aDecoder decodeObject];
    }
    return self;
    
}

- (void)encodeWithCoder:(NSCoder *)aCoder
{
    [aCoder encodeObject: title];
    [aCoder encodeObject: details];
    [aCoder encodeObject: location];
}

- (void)dealloc
{
    [title release];
    [details release];
    [location release];
    [super dealloc];
}

- (BOOL)isEqual:(id)other
{
    if ([other isKindOfClass: [POI class]] == NO)
        return NO;
    
    return (([title isEqual: [other title]]) && ([details isEqual: [other details]]) && ([location isEqual: [other location]]));
}

- (CLLocationCoordinate2D)coordinate
{
    return [location coordinate];
}

- (NSString*)description
{
    return [NSString stringWithFormat: @"(%f, %f): %@", (float)[location coordinate].latitude, (float)[location coordinate].longitude, title];
}

@end
