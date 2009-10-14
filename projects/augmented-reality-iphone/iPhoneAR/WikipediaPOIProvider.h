//
//  WikipediaPOIProvider.h
//  iPhoneAR
//
//  Created by Ben Gotow on 10/13/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import "TouchXML.h"
#import "POIProvider.h"
#import "POIQuadrant.h"
#import "POI.h"

@interface WikipediaPOIProvider : NSObject <POIProvider> {

    NSObject<POIProviderDelegate>   * delegate;
}

@property (nonatomic, assign) NSObject<POIProviderDelegate> * delegate;

- (id)init;

- (void)fetchQuadrant:(POIQuadrant*)q;
- (void)_fetchQuadrantXML:(POIQuadrant*)q;
- (void)_fetchQuadrantParseXMLResponse:(NSDictionary*)dict;

- (NSObject<POIProviderDelegate>*)delegate;
- (void)setDelegate:(NSObject<POIProviderDelegate>*)delegate;


@end
