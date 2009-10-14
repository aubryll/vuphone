//
//  POI.h
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 Gotow.net Creative. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

@interface POI : NSObject <NSCoding, MKAnnotation> {

    CLLocation  * location;
    NSString    * title;
    NSString    * details;
    
}

@property (nonatomic, retain) NSString * title;
@property (nonatomic, retain) NSString * details;
@property (nonatomic, retain) CLLocation * location;
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

- (id)initWithLocation:(CLLocation*)location;
- (id)initWithCoder:(NSCoder *)aDecoder;
- (void)encodeWithCoder:(NSCoder *)aCoder;
- (void)dealloc;

- (BOOL)isEqual:(id)other;
- (NSString*)description;

@end
