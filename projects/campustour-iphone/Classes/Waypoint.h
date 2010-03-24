//
//  Waypoint.h
//  Campus Tour
//
//  Created by sma1 on 2/9/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Mapkit/MKAnnotation.h>

@interface Waypoint : NSObject <MKAnnotation> {
	NSString *name;
	NSString *description;
	NSString *audioFilePath;
	NSString *funFacts;
	NSString *testimonials;
	int num;
	UIImage *image;
	CLLocationCoordinate2D coordinate;
}

@property (retain) NSString *name;
@property (retain) NSString *description;
@property (retain) NSString *audioFilePath;
@property (retain) NSString *funFacts;
@property (retain) NSString *testimonials;
@property (assign) int num;
@property (retain) UIImage *image;
@property (nonatomic, readonly) CLLocationCoordinate2D coordinate;

-(void)setLocation:(double)longitute latitude:(double)latitude;

@end
