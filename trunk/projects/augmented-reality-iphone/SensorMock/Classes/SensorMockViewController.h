//
//  SensorMockViewController.h
//  SensorMock
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <CoreLocation/CoreLocation.h>
#import <MessageUI/MessageUI.h>
#import "HTTPServer.h"

@interface SensorMockViewController : UIViewController <UIImagePickerControllerDelegate ,UINavigationControllerDelegate, CLLocationManagerDelegate, UIApplicationDelegate, UIAccelerometerDelegate, MFMailComposeViewControllerDelegate>{
    CLLocationManager * test;
    IBOutlet UILabel    * text;
    HTTPServer          * httpServer;
}

#pragma mark CLLocationManagerDelegate Implementation

- (void)locationManager:(CLLocationManager *)m didUpdateHeading:(CLHeading *)newHeading;
- (void)locationManager:(CLLocationManager *)m didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;

@end

