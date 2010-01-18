//
//  HourRange.h
//  Dining
//
//  Created by Aaron Thompson on 1/14/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>

#define ENTITY_NAME_HOUR_RANGE @"HourRange"

@class Restaurant;

@interface HourRange :  NSManagedObject  
{
}

@property (nonatomic, retain) NSNumber * openMinute;
@property (nonatomic, retain) NSNumber * closeMinute;
@property (nonatomic, retain) NSString * day;
@property (nonatomic, retain) Restaurant * restaurant;

- (NSNumber *)openHour;
- (NSNumber *)closeHour;
- (NSString *)formattedHoursString;

@end

