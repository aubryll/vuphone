//
//  POIProvider.h
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 Gotow.net Creative. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "POIQuadrant.h"

@protocol POIProviderDelegate

- (void)providerFetchedPOIs:(NSArray*)pois inQuadrant:(POIQuadrant*)q;

@end

@protocol POIProvider

- (void)fetchQuadrant:(POIQuadrant*)q;

- (NSObject<POIProviderDelegate>*)delegate;
- (void)setDelegate:(NSObject<POIProviderDelegate>*)delegate;

@end
