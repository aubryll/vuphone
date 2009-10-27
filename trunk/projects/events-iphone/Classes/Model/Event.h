//
//  Event.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <CoreData/CoreData.h>
#import <MapKit/MapKit.h>

@class Location;

@interface Event :  NSManagedObject <MKAnnotation>
{
	NSDateFormatter *dateFormatter;
}

@property (nonatomic, retain) NSString * ownerAndroidId;
@property (nonatomic, retain) NSString * source;
@property (nonatomic, retain) NSString * url;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSDate * startTime;
@property (nonatomic, retain) NSString * details;
@property (nonatomic, retain) NSDate * endTime;
@property (nonatomic, retain) Location * location;
@property (nonatomic, retain) NSString * serverId;

+ (NSArray *)allSources;
- (BOOL)isEditableByDeviceWithId:(NSString *)deviceId;
- (NSString *)title;
- (NSString *)subtitle;

@property (nonatomic, readonly, getter=startDateString) NSString *startDateString;
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

@end
