//
//  WikipediaPOIProvider.m
//  iPhoneAR
//
//  Created by Ben Gotow on 10/13/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "WikipediaPOIProvider.h"
#import "POIManager.h"

@implementation WikipediaPOIProvider 

@synthesize delegate;

- (id)init 
{
    if (self = [super init])
    {
    }
    return self;
}

- (void)dealloc
{
    // we do not retain the delegate to avoid a circular reference,
    // so there's no need to release it in the dealloc function.
    [super dealloc];
}

- (void)fetchQuadrant:(POIQuadrant*)q
{
    [self performSelectorInBackground:@selector(_fetchQuadrantXML:) withObject:q];
}

- (void)_fetchQuadrantXML:(POIQuadrant*)q
{
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    CGRect r = [[POIManager sharedManager] latLonRectForQuadrant: q];
    NSURL * url = [NSURL URLWithString: [NSString stringWithFormat:@"http://ws.geonames.org/findNearbyWikipedia?lat=%f&lng=%f&radius=2", r.origin.x + r.size.width / 2.0, r.origin.y + r.size.height / 2.0]];
    
    NSError * err = nil;
    NSData * data = [NSURLConnection sendSynchronousRequest:[NSURLRequest requestWithURL: url] returningResponse:nil error:&err];

    if (!data || err)
        NSLog(@"%@", [err description]);
    else
        [self performSelectorOnMainThread:@selector(_fetchQuadrantParseXMLResponse:) withObject:[NSDictionary dictionaryWithObjectsAndKeys:q,@"quadrant", data, @"data", nil] waitUntilDone:NO];
    
    [pool release];
}

- (void)_fetchQuadrantParseXMLResponse:(NSDictionary*)dict
{
    NSError * err = nil;
    CXMLDocument * doc = [[[CXMLDocument alloc] initWithData: [dict objectForKey:@"data"] options:0 error:&err] autorelease];
    
    if (err) {
        NSLog(@"%@", [err description]); 
        return;
    }
    
    CXMLElement * root = [doc rootElement];
    
    // find the pending quadrant and remove it from the list of pending quadrants
    POIQuadrant * quadrant = [dict objectForKey:@"quadrant"];
    NSArray * elements = [root elementsForName:@"entry"];
    NSMutableArray * pois = [NSMutableArray array];
        
    for (CXMLElement * element in elements){
        CLLocationDegrees lat = [[[[element elementsForName:@"lat"] lastObject] stringValue] doubleValue];
        CLLocationDegrees lon = [[[[element elementsForName:@"lng"] lastObject] stringValue] doubleValue];
        CLLocation * l = [[[CLLocation alloc] initWithLatitude:lat longitude:lon] autorelease];
        
        // because the wikipedia query takes a "radius" it's possible that points we get are outside our rectangle.
        // double check that the point falls within the correct bounds before we add it to the array.
        CGPoint p = [[POIManager sharedManager] quadrantForLatLon: CGPointMake(lat,lon)];
        
        if ((p.x == [quadrant x]) && (p.y == [quadrant y])) {
            POI * p = [[[POI alloc] initWithLocation: l] autorelease];
            [p setTitle: [[[element elementsForName: @"title"] lastObject] stringValue]];
            [p setDetails: [[[element elementsForName:@"summary"] lastObject] stringValue]];
            [pois addObject: p];
        }
    }
    
    [quadrant resetAge];
    [delegate providerFetchedPOIs:pois inQuadrant:quadrant];
}

@end
