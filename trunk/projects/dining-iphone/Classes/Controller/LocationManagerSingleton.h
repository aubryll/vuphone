//
//  LocationManagerSingleton.h
//  CampusMaps
//
//  Created by Demetri Miller on 12/25/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>


@interface LocationManagerSingleton : NSObject <CLLocationManagerDelegate> {

	CLLocationManager *locationManager;
	
}

@property (retain) CLLocationManager *locationManager; 

+ (id)sharedManager;
- (void)setupLocationManager;
- (CLLocation *)lastKnownLocation;

@end
