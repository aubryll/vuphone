//
//  RemoteEventLoader.h
//  Commencement
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DDXML.h"
#import "Event.h"
#import "Location.h"

#define EVENT_REQUEST_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8082/vandyupon/events"
#define EVENT_SUBMIT_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8082/vandyupon/events"
//#define EVENT_REQUEST_URL_STRING @"http://127.0.0.1:8080/vandyupon/events"
//#define EVENT_SUBMIT_URL_STRING @"http://127.0.0.1:8080/vandyupon/events"

@interface RemoteEventLoader : NSObject {

}

+ (NSArray *)getCommencementEventsFromServer:(NSManagedObjectContext *)context;
+ (NSArray *)getEventsFromServerBetween:(NSDate *)startDate and:(NSDate *)endDate updatedSince:(NSDate *)updated intoContext:(NSManagedObjectContext *)context;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoLocation:(Location *)location;
+ (void)submitEvent:(Event *)event;

@end
