//
//  MapViewController.h
//  Campus Tour
//
//  Created by Guy on 2/16/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>

@interface MapViewController : UIViewController {

	IBOutlet MKMapView* mapView;

}

- (IBAction)centerOnCampus:(id)sender;
@end
