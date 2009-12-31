//
//  iPhoneARViewController.m
//  iPhoneAR
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright Gotow.net Creative 2009. All rights reserved.
//

#import "ARViewController.h"
#import "Constants.h"
#import "WikipediaPOIProvider.h"
#import "WebPOIProvider.h"
#import "LayersViewController.h"

static float lastHeading;

@implementation ARViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}

- (void)dealloc 
{
    [quadrantAnnotations release];
    [locationManager release];
    [locationAnnotation release];
    [super dealloc];
}

- (void)viewDidLoad 
{
    [super viewDidLoad];
    [map setDelegate: self];
    [map setZoomEnabled: NO];
    
    // setup updates from the CLLocationManager
    locationManager = [[CLLocationManager alloc] init];
    [locationManager setDelegate: self];
    //[locationManager startUpdatingHeading];
    //[locationManager startUpdatingLocation];
    locationAnnotation = [[UserLocationAnnotation alloc] init];
    [map addAnnotation: locationAnnotation];
    
    locationAnnotationView = [[MKAnnotationView alloc] initWithAnnotation:locationAnnotation reuseIdentifier: @"UserLocation"];
    [locationAnnotationView setImage: [UIImage imageNamed:@"UserLocation.png"]];

    // initialize our POIManager and create layers with providers that will fetch the points as they are needed
    WikipediaPOIProvider * wikiProvider = [[[WikipediaPOIProvider alloc] init] autorelease];
    POILayer * wikiLayer = [[[POILayer alloc] initWithName:@"Wikipedia" andProvider: wikiProvider andCacheLifespan: 60 * 60 * 60 * 24] autorelease];
    [[POIManager sharedManager] addLayer: wikiLayer];
    
    WebPOIProvider * vuseProvider = [[[WebPOIProvider alloc] initWithProviderURL: @"http://www.gotow.net/"] autorelease];
    POILayer * vuseLayer = [[[POILayer alloc] initWithName:@"Vanderbilt" andProvider: vuseProvider andCacheLifespan: 60 * 60 * 10] autorelease];
    [[POIManager sharedManager] addLayer: vuseLayer];
    
    // register to recieve updates from the POIManager when points need to be added or removed from display
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(addPoints:) name:kNotificationPointsAdded object: nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(removePoints:) name:kNotificationPointsRemoved object: nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(quadrantAdded:) name:kNotificationQuadrantAdded object: nil];

    // Initialize to a fake starting position
    CLLocation * updated = [[CLLocation alloc] initWithLatitude:37.37848900 longitude:-121.98393100];
    [self locationManager:locationManager didUpdateToLocation:updated fromLocation:nil];
        
    // start faking location updates
    [NSTimer scheduledTimerWithTimeInterval:0.05 target:self selector:@selector(move) userInfo:nil repeats:YES];
}

#pragma mark User Interaction

- (IBAction)setSide:(id)sender
{
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration: 1.0];
    if ([sender selectedSegmentIndex] == 0){
        [UIView setAnimationTransition:UIViewAnimationTransitionFlipFromLeft forView:flipContainer cache:YES];
        [cameraSide removeFromSuperview];
        [flipContainer addSubview: mapSide];
    } else {
        [UIView setAnimationTransition:UIViewAnimationTransitionFlipFromRight forView:flipContainer cache:YES];
        [mapSide removeFromSuperview];
        [flipContainer addSubview: cameraSide];
    }
    [UIView commitAnimations];
}

- (IBAction)setVisibleLayers:(id)sender
{
    LayersViewController * b = [[[LayersViewController alloc] initWithNibName:@"LayersViewController" bundle:nil] autorelease];
    [self presentModalViewController:b animated:YES];
}

- (void)move
{
    
    CLLocation * current = [[POIManager sharedManager] center];
    if (current != nil){
        CLLocation * updated = [[CLLocation alloc] initWithLatitude:[current coordinate].latitude + 0.0002 longitude:[current coordinate].longitude + 0.0002];
        [self locationManager:locationManager didUpdateToLocation:updated fromLocation:current];
    }
    /*
    lastHeading += 0.008;
    if (lastHeading > 2 * M_PI)
        lastHeading = 0;
    [cameraOverlayContainer setHeadingRadians: lastHeading];
    [locationAnnotationView setTransform: CGAffineTransformMakeRotation(lastHeading)];
    */
}

- (void)didReceiveMemoryWarning 
{
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload 
{
}

#pragma mark CLLocationManager Delegate Functions

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;
{
    [[POIManager sharedManager] setCenter:newLocation andDesiredPOIRadius: 1609.344 * 1.5]; // mile and a half
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance([newLocation coordinate], 1609.344 * 3, 1609.344 * 3);
    [map setRegion:region animated:YES];
    [cameraOverlayContainer setCenterLocation: newLocation];
    [locationAnnotation setCoordinate: newLocation.coordinate];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading
{
    [cameraOverlayContainer setHeadingRadians: [newHeading trueHeading]];
}

#pragma mark POIManager Notification Callbacks

- (void)addPoints:(NSNotification*)notification
{
    NSArray * points = [notification object];
    NSLog(@"Notification: Adding Points: %@", [points description]);
    [map addAnnotations: points];
    [cameraOverlayContainer addAnnotations: points];
}

- (void)removePoints:(NSNotification*)notification
{
    NSArray * points = [notification object];
    NSLog(@"Notification: Removing Points: %@", [points description]);
    [map removeAnnotations: points];
    [cameraOverlayContainer removeAnnotations: points];
}

- (void)quadrantAdded:(NSNotification*)notification
{
    POIQuadrant * q = [notification object];
    [map addAnnotation: q];
    [cameraOverlayContainer addAnnotation: q];
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    if ([annotation isKindOfClass: [UserLocationAnnotation class]]) {
        return locationAnnotationView;
    
    } else if ([annotation isKindOfClass: [POIQuadrant class]]){
        MKAnnotationView * view = [mapView dequeueReusableAnnotationViewWithIdentifier: @"POIQuadrant"];
        if (view == nil){
            view = [[[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier: @"POIQuadrant"] autorelease];
            [view setImage: [UIImage imageNamed:@"QuadrantAnnotation.png"]];
        }
        return view;
    }
    return nil;
}

@end
