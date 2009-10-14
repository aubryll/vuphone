//
//  WebPOIProvider.m
//  iPhoneAR
//
//  Created by Ben Gotow on 10/1/09.
//  Copyright 2009 Gotow.net Creative. All rights reserved.
//

#import "WebPOIProvider.h"

@implementation WebPOIProvider

@synthesize delegate;

- (id)initWithProviderURL:(NSString*)url
{
    if (self = [super init]) {
        providerURLString = [url retain];
        pendingQuadrants = [[NSMutableDictionary alloc] init];
    }
    return self;
}

- (void)dealloc
{
    // we do not retain the delegate to avoid a circular reference,
    // so there's no need to release it in the dealloc function.
    [pendingQuadrants release];
    [providerURLString release];
    [super dealloc];
}

- (void)fetchQuadrant:(POIQuadrant*)q
{
    if ([pendingQuadrants objectForKey: [q key]] != nil){
        NSLog(@"Ignoring duplicate request for quadrant %@", [q key]);
        return;
    }
    
    [pendingQuadrants setObject:q forKey:[q key]];
    //[self performSelectorInBackground: @selector(_fetchQuadrantXML:) withObject:q];
    NSData * d = [NSData dataWithContentsOfFile:@"/Users/bengotow/Desktop/sample_points.xml"];
    [self _fetchQuadrantParseXMLResponse: d];
}

- (void)_fetchQuadrantXML:(POIQuadrant*)q
{
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    
    NSError * err = nil;
    NSURL * url = [NSURL URLWithString: [NSString stringWithFormat:@"%@?x=%d&y=%d&format=xml", providerURLString, [q x], [q y]] ];
    NSURLRequest * req = [NSURLRequest requestWithURL: url];
    NSData * data = [NSURLConnection sendSynchronousRequest:req returningResponse:NULL error:&err];
    
    if (!data || err)
        NSLog(@"%@", [err description]);
    else
        [self performSelectorOnMainThread:@selector(_fetchQuadrantParseXMLResponse:) withObject:data waitUntilDone:NO];
    
    [pool release];
}

- (void)_fetchQuadrantParseXMLResponse:(NSData*)d
{
    NSError * err = nil;
    CXMLDocument * doc = [[[CXMLDocument alloc] initWithData:d options:0 error:&err] autorelease];
    
    if (err) {
        NSLog(@"%@", [err description]); 
        return;
    }
    
    CXMLElement * root = [doc rootElement];
    
    // find the pending quadrant and remove it from the list of pending quadrants
    NSString * quadrantKey = [NSString stringWithFormat:@"%@,%@", [[root attributeForName:@"x"] stringValue], [[root attributeForName:@"y"] stringValue]];
    POIQuadrant * quadrant = [pendingQuadrants objectForKey: quadrantKey];
    if (quadrant)
        NSLog(@"Parsing response for quadrant %@", quadrantKey);
    else
        NSLog(@"Received response for quadrant that was never requested.");

    NSArray * elements = [root elementsForName:@"POI"];
    NSMutableArray * pois = [NSMutableArray array];
    
    for (CXMLElement * element in elements){
        CLLocationDegrees lat = [[[element attributeForName:@"lat"] stringValue] doubleValue];
        CLLocationDegrees lon = [[[element attributeForName:@"lon"] stringValue] doubleValue];
        CLLocation * l = [[[CLLocation alloc] initWithLatitude:lat longitude:lon] autorelease];
        
        POI * p = [[[POI alloc] initWithLocation: l] autorelease];
        [p setTitle: [[element attributeForName: @"name"] stringValue]];
        [p setDetails: [[[element elementsForName:@"description"] lastObject] stringValue]];
        [pois addObject: p];
    }
    
    [delegate providerFetchedPOIs:pois inQuadrant:quadrant];
    [pendingQuadrants removeObjectForKey: quadrantKey];
}

@end
