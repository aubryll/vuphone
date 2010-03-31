//
//  RemoteEventLoader.h
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>


@interface WaypointXMLReader : NSObject {

}

+ (NSArray *)waypointsFromXMLAtPath:(NSString *)path;
//+ (void)getDataFromXMLNode:(DDXMLNode *)node intoWaypoint:(Waypoint *)waypoint;

@end
