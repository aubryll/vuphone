//
//  Restaurant.h
//  Dining
//
//  Created by Aaron Thompson on 1/10/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

#define ENTITY_NAME_RESTAURANT @"Restaurant"

@class HourRange;

@interface Restaurant : NSManagedObject <MKAnnotation>
{
	UIImage *_image;
}

@property (nonatomic, retain) NSNumber * acceptsMealPlan;
@property (nonatomic, retain) NSString * details;
@property (nonatomic, retain) NSString * imageUrlString;
@property (nonatomic, retain) NSString * phone;
@property (nonatomic, retain) NSNumber * offCampus;
@property (nonatomic, retain) NSNumber * longitude;
@property (nonatomic, retain) NSNumber * latitude;
@property (nonatomic, retain) NSString * type;
@property (nonatomic, retain) NSNumber * acceptsMealMoney;
@property (nonatomic, retain) NSString * urlString;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSNumber * minutesUntilClose;
@property (nonatomic, retain) NSNumber * isClosed;
@property (nonatomic, retain) NSNumber * distanceInFeet;
@property (nonatomic, retain) NSSet * openHours;

@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;


- (void)deleteAllOpenHours;
+ (Restaurant *)restaurantWithName:(NSString *)aName inContext:(NSManagedObjectContext *)context;
- (UIImage *)image;
- (NSString *)distanceAsString;
+ (NSString *)prettyDistance:(CLLocationDistance)distanceInFeet;

@end


@interface Restaurant (CoreDataGeneratedAccessors)
- (void)addOpenHoursObject:(HourRange *)value;
- (void)removeOpenHoursObject:(HourRange *)value;
- (void)addOpenHours:(NSSet *)value;
- (void)removeOpenHours:(NSSet *)value;

@end

