//
//  Event.h
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>

#define EVENT_ERROR_DOMAIN @"EVENT_ERROR_DOMAIN"

enum EVENT_CODES {
	SERVER_ID_INVALID_CODE = 1,
	SERVER_ID_EXISTS_CODE = 2
};

@class Location;

@interface Event :  NSManagedObject <MKAnnotation>
{
}

@property (nonatomic, retain) NSString * ownerDeviceId;
@property (nonatomic, retain) NSString * source;
@property (nonatomic, retain) NSString * url;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSDate * startTime;
@property (nonatomic, retain) NSString * details;
@property (nonatomic, retain) NSDate * endTime;
@property (nonatomic, retain) Location * location;
@property (nonatomic, retain) NSString * serverId;
@property (nonatomic, retain) NSSet* ratings;

+ (NSArray *)allSources;
- (BOOL)isEditableByDeviceWithId:(NSString *)deviceId;
+ (BOOL)duplicateServerId:(NSString *)anId existsInContext:(NSManagedObjectContext *)context;
+ (Event *)eventWithServerId:(NSString *)anId inContext:(NSManagedObjectContext *)context;
- (NSString *)title;
- (NSString *)subtitle;

@property (nonatomic, readonly, getter=startDateString) NSString *startDateString;
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

@end

@interface Event (CoreDataGeneratedAccessors)

- (void)addEventsObject:(Event *)value;
- (void)removeEventsObject:(Event *)value;
- (void)addEvents:(NSSet *)value;
- (void)removeEvents:(NSSet *)value;

@end
