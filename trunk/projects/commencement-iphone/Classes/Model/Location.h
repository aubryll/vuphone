//
//  Location.h
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>

@class Event;

@interface Location :  NSManagedObject <MKAnnotation>
{
}

+ (NSArray *)rootLocations:(NSManagedObjectContext *)context;
+ (Location *)locationWithServerId:(NSString *)anId inContext:(NSManagedObjectContext *)context;
- (BOOL)isEditableByDeviceWithId:(NSString *)deviceId;

@property (nonatomic, retain) NSString * ownerDeviceId;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSDecimalNumber * longitude;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSDecimalNumber * latitude;
@property (nonatomic, retain) NSSet* events;
@property (nonatomic, retain) NSSet* childLocations;
@property (nonatomic, retain) Location * parentLocation;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

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

