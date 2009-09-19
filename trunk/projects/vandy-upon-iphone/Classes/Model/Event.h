//
//  Event.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <CoreData/CoreData.h>

@class Location;

@interface Event :  NSManagedObject  
{
}

@property (nonatomic, retain) NSString * ownerAndroidId;
@property (nonatomic, retain) NSString * url;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSDate * startTime;
@property (nonatomic, retain) NSString * details;
@property (nonatomic, retain) NSDate * endTime;
@property (nonatomic, retain) Location * location;

@end
