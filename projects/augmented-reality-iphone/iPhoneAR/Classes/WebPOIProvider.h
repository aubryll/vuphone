//
//  WebPOIProvider.h
//  iPhoneAR
//
//  Created by Ben Gotow on 10/1/09.
//  Copyright 2009 Gotow.net Creative. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "TouchXML.h"
#import "POIProvider.h"
#import "POIQuadrant.h"
#import "POI.h"

@interface WebPOIProvider : NSObject <POIProvider> {

    NSMutableDictionary             * pendingQuadrants;
    NSString                        * providerURLString;
    NSObject<POIProviderDelegate>   * delegate;
}

@property (nonatomic, assign) NSObject<POIProviderDelegate> * delegate;

- (id)initWithProviderURL:(NSString*)url;

- (void)fetchQuadrant:(POIQuadrant*)q;
- (void)_fetchQuadrantXML:(POIQuadrant*)q;
- (void)_fetchQuadrantParseXMLResponse:(NSData*)d;

@end
