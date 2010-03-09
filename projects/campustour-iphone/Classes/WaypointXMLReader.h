//
//  RemoteEventLoader.h
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DDXML.h"
#import "DDXMLNode.h"

@interface WaypointXMLReader : NSObject {

}

+ (NSArray *)waypointsFromXMLAtPath:(NSString *)path;
+ (NSString *)getXMLData:(DDXMLNode *)node tag:(NSString *)tagName attribute:(NSString *)attr;
//+ (void)getDataFromXMLNode:(DDXMLNode *)node intoWaypoint:(Waypoint *)waypoint;

@end
