//
//  MapViewController.h
//  CampusMaps
//
//  Created by Aaron Thompson on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

#define CAMPUS_CENTER_LATITUDE 36.146671
#define CAMPUS_CENTER_LONGITUDE -86.803709

@interface MapViewController : UIViewController {
	IBOutlet MKMapView *mapView;
}

@end
