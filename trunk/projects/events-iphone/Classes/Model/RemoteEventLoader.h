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
#import "Location.h"

#define EVENT_REQUEST_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8081/vandyupon/events"
#define EVENT_SUBMIT_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8081/vandyupon/events"

@interface RemoteEventLoader : NSObject {

}

+ (NSArray *)eventsFromServerWithContext:(NSManagedObjectContext *)context;
+ (NSArray *)eventsFromServerSince:(NSDate *)date withContext:(NSManagedObjectContext *)context;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event;
+ (void)submitEvent:(Event *)event;

@end
