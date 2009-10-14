//
//  iPhoneARViewController.h
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright Gotow.net Creative 2009. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>
#import "POIManager.h"

@interface ARViewController : UIViewController <CLLocationManagerDelegate> {

    CLLocationManager   * locationManager;
    IBOutlet MKMapView  * map;
    
    NSMutableDictionary * quadrantAnnotations;
}

#pragma mark CLLocationManager Delegate Functions

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;
- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading;

@end
