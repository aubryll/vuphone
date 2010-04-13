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

// TODO - this works fully versus the localhost8082 address that does not. Need to download both and diff them (after
// running the request script on both to make sure they are exactly the same data! (thought - but what if the request script is the 
// issue - do NOT run this on the live DB, only run the updates on the local DB
//#define EVENT_REQUEST_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8082/vandyupon/events"
//#define EVENT_SUBMIT_URL_STRING @"http://afrl-gift.dre.vanderbilt.edu:8082/vandyupon/events"
#define EVENT_REQUEST_URL_STRING @"http://127.0.0.1:8082/vandyupon/events"
#define EVENT_SUBMIT_URL_STRING @"http://127.0.0.1:8082/vandyupon/events"

@interface RemoteEventLoader : NSObject {

}

+ (NSArray *)getCommencementEventsFromServerSince:(NSDate *)updated intoContext:(NSManagedObjectContext *)context;
+ (NSArray *)getEventsFromServerBetween:(NSDate *)startDate and:(NSDate *)endDate updatedSince:(NSDate *)updated intoContext:(NSManagedObjectContext *)context;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoLocation:(Location *)location;
+ (void)submitEvent:(Event *)event;

@end
