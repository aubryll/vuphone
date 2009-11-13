//
//  EventRating.h
//  Events
//
//  Created by Aaron Thompson on 11/12/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>

@class Event;

@interface EventRating :  NSManagedObject  
{
}

@property (nonatomic, retain) NSNumber * score;
@property (nonatomic, retain) NSString * comment;
@property (nonatomic, retain) NSDate * submissionDate;
@property (nonatomic, retain) Event * event;

@end
