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
#import "Event.h"
#import "Location.h"

//#define EVENT_REQUEST_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8081/Events/events"
//#define EVENT_SUBMIT_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8081/Events/events"
#define EVENT_REQUEST_URL_STRING @"http://129.59.82.65:8080/vandyupon/events"
#define EVENT_SUBMIT_URL_STRING @"http://129.59.82.65:8080/vandyupon/events"

@interface RemoteEventLoader : NSObject {

}

+ (NSArray *)eventsFromServerWithContext:(NSManagedObjectContext *)context;
+ (NSArray *)getEventsFromServerSince:(NSDate *)date intoContext:(NSManagedObjectContext *)context;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event;
+ (void)submitEvent:(Event *)event;

@end
