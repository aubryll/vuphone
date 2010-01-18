//
//  MapViewController.h
//  Dining
//
//  Created by Aaron Thompson on 1/11/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "Restaurant.h"

#define CAMPUS_CENTER_LATITUDE 36.142
#define CAMPUS_CENTER_LONGITUDE -86.8044

@interface MapViewController : UIViewController <MKMapViewDelegate> {
	IBOutlet MKMapView *mapView;
}

- (void)selectRestaurant:(Restaurant *)restaurant;

@end
