//
//  Location.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <CoreData/CoreData.h>

@class Event;

@interface Location :  NSManagedObject  
{
}

+ (NSArray *)rootLocations:(NSManagedObjectContext *)context;

@property (nonatomic, retain) NSDecimalNumber * longitude;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSDecimalNumber * latitude;
@property (nonatomic, retain) NSSet* events;
@property (nonatomic, retain) NSSet* childLocations;
@property (nonatomic, retain) Location * parentLocation;

@end


@interface Location (CoreDataGeneratedAccessors)
- (void)addEventsObject:(Event *)value;
- (void)removeEventsObject:(Event *)value;
- (void)addEvents:(NSSet *)value;
- (void)removeEvents:(NSSet *)value;

- (void)addChildLocationsObject:(Location *)value;
- (void)removeChildLocationsObject:(Location *)value;
- (void)addChildLocations:(NSSet *)value;
- (void)removeChildLocations:(NSSet *)value;

@end

