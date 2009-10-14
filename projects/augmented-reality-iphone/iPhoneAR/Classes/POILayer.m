//
//  POILayer.m
//  iPhoneAR
//
//  Created by Ben Gotow on 10/13/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POILayer.h"
#import "POIManager.h"
#import "Constants.h"

@implementation POILayer

@synthesize provider;
@synthesize displayed;
@synthesize name;

- (id)initWithName:(NSString *)n andProvider:(NSObject<POIProvider>*)p andCacheLifespan:(NSTimeInterval)l
{
    if (self = [super init]){
        cacheLifespan = l;
        cacheQuadrants = [[NSMutableDictionary alloc] init];
        provider = [p retain];
        name = [n retain];
        displayed = YES;
        
        [provider setDelegate: self];
    }
    return self;
}

- (void)dealloc
{
    [provider release];
    [cacheQuadrants release];
    [name release];
    [super dealloc];
}

#pragma mark Managing POI Cache

- (void)clearCache
{
    [cacheQuadrants removeAllObjects];
}

- (void)setCacheLifespan:(NSTimeInterval)seconds
{
    cacheLifespan = seconds;
    
    for (NSString * key in cacheQuadrants)
        if ([(POIQuadrant*)[cacheQuadrants objectForKey: key] age] > cacheLifespan)
            [cacheQuadrants removeObjectForKey: key];
}

#pragma mark Updating / Displaying POIs

- (void)setVisibleQuadrants:(CGRect)visible lastVisibleQuadrants:(CGRect)lastVisible
{
    CGRect stillVisible = CGRectIntersection(visible, lastVisible);

    // iterate over the quadrants represented in the rect and put them into the display dictionary
    for (int x = visible.origin.x; x < visible.origin.x + visible.size.width; x++){
        for (int y = visible.origin.y; y < visible.origin.y + visible.size.height; y++){
     
            // find the quadrant if it's already been cached
            NSString * key = [NSString stringWithFormat:@"%d,%d", x, y];
            POIQuadrant * q = [cacheQuadrants objectForKey: key];
            
            if (!q){
                NSLog(@"Creating quadrant %@", key);
                q = [[[POIQuadrant alloc] initWithX:x andY:y] autorelease];
                [cacheQuadrants setObject:q forKey:key];
                [[NSNotificationCenter defaultCenter] postNotificationName:kNotificationQuadrantAdded object:q];
            }
            
            if ([q age] > cacheLifespan)
                [[self provider] fetchQuadrant: q];
                
            // if the quadrant was not visible already, post a notification telling our observers to display points in this quadrant (if any exist)
            else if (!CGRectContainsPoint(stillVisible, CGPointMake(x, y)) && ([[q points] count] > 0))
                [[NSNotificationCenter defaultCenter] postNotificationName: kNotificationPointsAdded object: [q points]];
        }
    }
    
    // iterate over the quadrants that used to be visible. If any are no longer visible, tell our observers to remove them from view
    for (int x = lastVisible.origin.x; x < lastVisible.origin.x + lastVisible.size.width; x++){
        for (int y = lastVisible.origin.y; y < lastVisible.origin.y + lastVisible.size.height; y++){
        
            // find the quadrant
            NSString * key = [NSString stringWithFormat:@"%d,%d", x, y];
            POIQuadrant * q = [cacheQuadrants objectForKey: key];
            
            if (!CGRectContainsPoint(stillVisible, CGPointMake(x, y)) && ([[q points] count] > 0))
                [[NSNotificationCenter defaultCenter] postNotificationName: kNotificationPointsRemoved object: [q points]];
        }
    }
}


#pragma mark Provider Delegate Functions

- (void)providerFetchedPOIs:(NSArray*)newPoints inQuadrant:(POIQuadrant*)q
{
    NSAutoreleasePool * p = [[NSAutoreleasePool alloc] init];
    
    // if the quadrant is not currently visible, we can just replace it's entire POI array quietly.
    if ([[POIManager sharedManager] isQuadrantWithinVisibleRadius: q] == NO) {
        [q setPoints: [NSMutableArray arrayWithArray: newPoints]];
    
    } else {
        // if the quadrant is being drawn, we want to carefully iterate through the new points and see which
        // have changed or disappeared from the quadrant's POI list. This is expensive, but it is much
        // more efficient than destroying all of the overlays in the view hierarchy and recreating them
        // with the same content. That could take 100s of msec and result in a visual jump...
        
        NSMutableArray * matchedOldPoints = [NSMutableArray array];
        NSMutableArray * unmatchedNewPoints = [NSMutableArray arrayWithArray: newPoints];
        
        for (POI * p in newPoints){
            int existingPointIndex = [[q points] indexOfObject: p];
            
            if (existingPointIndex != NSNotFound){
                // we've found an old point that isEqual: to the new point. The new point is
                // no longer significant.
                [matchedOldPoints addObject: [[q points] objectAtIndex: existingPointIndex]];
                [unmatchedNewPoints removeObject: p];
            }
        }
        
        // At this point we have:
        //   matchedOldPoints --> objects from the old array that are still in the new array
        //   ([q points] - matchedOldPoints) --> objects from the old array that do not match anything in the new array
        //   unmatchedNewPoints --> points that are new or modified
        
        // Calculate ([q points] - matchedOldPoints)
        NSMutableArray * removed = [NSMutableArray arrayWithArray: [q points]];
        [removed removeObjectsInArray: matchedOldPoints];
        
        // Commit changes
        NSMutableArray * newPoints = [NSMutableArray array];
        [newPoints addObjectsFromArray: matchedOldPoints];
        [newPoints addObjectsFromArray: unmatchedNewPoints];
        [q setPoints: newPoints];
        
        // Send notifications to alert the UI of changes
        if ([removed count] > 0)
            [[NSNotificationCenter defaultCenter] postNotificationName: kNotificationPointsRemoved object: removed];
        if ([unmatchedNewPoints count] > 0)
            [[NSNotificationCenter defaultCenter] postNotificationName: kNotificationPointsAdded object: unmatchedNewPoints];
    }
    
    [p release];
}

@end
