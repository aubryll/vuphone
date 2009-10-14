//
//  POILayer.h
//  iPhoneAR
//
//  Created by Ben Gotow on 10/13/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "POIProvider.h"
#import "POI.h"

@interface POILayer : NSObject <POIProviderDelegate> {

    NSString            * name;
    BOOL                  displayed;
    NSObject<POIProvider>* provider;
    
    NSTimeInterval        cacheLifespan;
    NSMutableDictionary * cacheQuadrants;
}

@property (nonatomic, retain) NSObject<POIProvider>* provider;
@property (nonatomic, assign) BOOL displayed;
@property (nonatomic, retain) NSString * name;

- (id)initWithName:(NSString *)n andProvider:(NSObject<POIProvider>*)p andCacheLifespan:(NSTimeInterval)l;
- (void)dealloc;

#pragma mark Managing POI Cache

- (void)clearCache;
- (void)setCacheLifespan:(NSTimeInterval)seconds;

#pragma mark Updating / Displaying POIs

- (void)setVisibleQuadrants:(CGRect)visible lastVisibleQuadrants:(CGRect)lastVisible;

#pragma mark Provider Delegate Functions

- (void)providerFetchedPOIs:(NSArray*)newPoints inQuadrant:(POIQuadrant*)q;

@end
