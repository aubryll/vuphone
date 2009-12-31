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
#import "AROverlayContainerView.h"
#import "UserLocationAnnotation.h"

@interface ARViewController : UIViewController <CLLocationManagerDelegate, MKMapViewDelegate> {
    
    CLLocationManager                   * locationManager;
    UserLocationAnnotation              * locationAnnotation;
    MKAnnotationView                    * locationAnnotationView;
    
    IBOutlet UIView                     * flipContainer;
    IBOutlet UIView                     * mapSide;
    IBOutlet MKMapView                  * map;
    IBOutlet UIView                     * cameraSide;
    IBOutlet AROverlayContainerView     * cameraOverlayContainer;
    IBOutlet UIBarButtonItem            * layersButton;
    
    NSMutableDictionary                 * quadrantAnnotations;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil;
- (void)viewDidLoad;

#pragma mark User Interaction

- (IBAction)setSide:(id)sender;
- (IBAction)setVisibleLayers:(id)sender;

#pragma mark CLLocationManager Delegate Functions

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;
- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading;

@end
