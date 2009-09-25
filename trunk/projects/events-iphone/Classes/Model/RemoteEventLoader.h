//
//  RemoteEventLoader.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DDXML.h"
#import "Event.h"

@interface RemoteEventLoader : NSObject {

}

+ (NSArray *)eventsFromServerWithContext:(NSManagedObjectContext *)context;
+ (NSArray *)eventsFromServerSince:(NSDate *)date withContext:(NSManagedObjectContext *)context;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event;

@end
