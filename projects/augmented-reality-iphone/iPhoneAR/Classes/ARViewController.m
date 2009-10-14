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

@implementation ARViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad 
{
    [super viewDidLoad];
    [map setDelegate: self];
    [map setShowsUserLocation: YES];
    [map setZoomEnabled: NO];
    
    // setup updates from the CLLocationManager
    locationManager = [[CLLocationManager alloc] init];
    [locationManager setDelegate: self];
    [locationManager startUpdatingHeading];
    [locationManager startUpdatingLocation];
    
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

    // start faking location updates
    [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(move) userInfo:nil repeats:YES];
}

- (void)move
{
    CLLocation * current = [[POIManager sharedManager] center];
    if (current != nil){
        CLLocation * updated = [[CLLocation alloc] initWithLatitude:[current coordinate].latitude + 0.0005 longitude:[current coordinate].longitude + 0.0005];
        [self locationManager:locationManager didUpdateToLocation:updated fromLocation:current];
    }
}

- (void)didReceiveMemoryWarning 
{
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload 
{
}

- (void)dealloc 
{
    [super dealloc];
}

#pragma mark CLLocationManager Delegate Functions

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;
{
    [[POIManager sharedManager] setCenter:newLocation andDesiredPOIRadius: 1609.344 * 1.5]; // mile and a half
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance([newLocation coordinate], 1609.344 * 3, 1609.344 * 3);
    [map setRegion:region animated:YES];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading
{
}

#pragma mark POIManager Notification Callbacks

- (void)addPoints:(NSNotification*)notification
{
    NSArray * points = [notification object];
    NSLog(@"Notification: Adding Points: %@", [points description]);
    [map addAnnotations: points];
}

- (void)removePoints:(NSNotification*)notification
{
    NSArray * points = [notification object];
    NSLog(@"Notification: Removing Points: %@", [points description]);
    [map removeAnnotations: points];
}

- (void)quadrantAdded:(NSNotification*)notification
{
    POIQuadrant * q = [notification object];
    [map addAnnotation: q];
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation
{
    if ([annotation isKindOfClass: [POIQuadrant class]]){
        MKAnnotationView * view = [mapView dequeueReusableAnnotationViewWithIdentifier: @"POIQuadrant"];
        if (view == nil){
            view = [[[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier: @"POIQuadrant"] autorelease];
            [view setImage: [UIImage imageNamed:@"QuadrantAnnotation.png"]];
        }
        return view;
    }
}

@end
