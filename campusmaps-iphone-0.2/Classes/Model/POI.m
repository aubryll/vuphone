// 
//  POI.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/16/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POI.h"
#import "Layer.h"
#import "NSManagedObjectContext-Convenience.h"
#import "LocationManagerSingleton.h"
#include <math.h>

@implementation POI 

@dynamic longitude;
@dynamic latitude;
@dynamic layer;
@dynamic name;
@dynamic subtitle;
@dynamic details;
@dynamic serverId;
@dynamic url;

@synthesize imageLoadingState;

- (void)awakeFromInsert
{
	[super awakeFromInsert];
	imageLoadingState = POIImageNotYetLoadingState;
}

- (void)awakeFromFetch
{
	[super awakeFromFetch];
	imageLoadingState = POIImageNotYetLoadingState;
}


+ (POI *)POIWithServerId:(NSString *)anId inContext:(NSManagedObjectContext *)context {
	NSSet *POIs = [context fetchObjectsForEntityName:ENTITY_NAME_POI withPredicateString:@"serverId = %@", anId];
	return [POIs anyObject];
}

/**
 * Copied from the Android Campus Maps
 * Method for converting from EPSG900913 format used by vu.gml to latitude
 * and longitude. Based on reversing a C# function from
 * http://www.cadmaps.com/gisblog/?cat=10
 * 
 * @param x
 *            - 1st EPSG900913 coordinate
 * @param y
 *            - 2nd EPSG900913 coordinate
 */
- (void)setEPSG900913CoordinatesLat:(double)x andLon:(double)y
{
	double lon = x / (6378137.0 * M_PI / 180);
	double lat = ((atan(pow(M_E, (y / 6378137.0))))
				  / (M_PI / 180) - 45) * 2.0;
	
	self.latitude = [NSDecimalNumber decimalNumberWithDecimal:[[NSNumber numberWithDouble:lat] decimalValue]];
	self.longitude = [NSDecimalNumber decimalNumberWithDecimal:[[NSNumber numberWithDouble:lon] decimalValue]];
}


-(CLLocationCoordinate2D)coordinate {
	CLLocationCoordinate2D coord;
	
	//[self willAccessValueForKey:@"longitude"];
	coord.longitude = [self.longitude doubleValue];
	//[self didAccessValueForKey:@"longitude"];
	
	//[self willAccessValueForKey:@"latitude"];
	coord.latitude = [self.latitude doubleValue];
	//[self didAccessValueForKey:@"latitude"];
	
	return coord;
}

- (NSString *)title {
	return self.name;
}

// Returns the image for this POI, whose URL is specified in the url property
- (UIImage *)image 
{
	if (!_image && imageLoadingState != POIImageFailedToLoadState) {
		[self loadImage];
	}
	return _image;
}

- (void)loadImage
{
	NSString *urlString = [NSString stringWithFormat:@"%@%@", BASE_IMAGE_URL_STRING, self.url];
	imageLoadingState = POIImageIsLoadingState;
	NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:urlString]];
	if (imageData != nil) {
		_image = [[UIImage alloc] initWithData:imageData];
		imageLoadingState = POIImageLoadedState;	
	} else {
		_image = nil;
		imageLoadingState = POIImageFailedToLoadState;
	}
}

// Returns the distance to the POI from the location specified...Formatted as a string.
- (NSString *)distanceFromLocation:(CLLocation *)location
{
	CLLocation *poiLocation = [[[CLLocation alloc] initWithLatitude:[self.latitude doubleValue] 
														  longitude:[self.longitude doubleValue]] autorelease];
	// Distance measured in meters. 
	CLLocationDistance distance = [poiLocation getDistanceFrom:location];
	
	// Convert distance to miles.
	distance = distance * 0.000621371192;
	
	// Round off double to scale decimal places.
	NSDecimalNumberHandler *mydnh = [[[NSDecimalNumberHandler alloc] initWithRoundingMode:NSRoundPlain 
																					scale:2
																		 raiseOnExactness:NO 
																		  raiseOnOverflow:NO 
																		 raiseOnUnderflow:NO 
																	  raiseOnDivideByZero:NO] autorelease];
	NSDecimalNumber *myDecimal = [[[NSDecimalNumber alloc] initWithDouble:distance] autorelease];
	NSDecimalNumber *result = [myDecimal decimalNumberByRoundingAccordingToBehavior:mydnh];
	
	return [NSString stringWithFormat:@"%@ miles", result];
}

@end
