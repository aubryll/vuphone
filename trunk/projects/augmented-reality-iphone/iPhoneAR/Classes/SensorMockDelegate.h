//
//  SensorMockDelegate.h
//  SensorMock
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreLocation/CoreLocation.h>

@interface SensorMockDelegate : NSObject <CLLocationManagerDelegate> {

    id<CLLocationManagerDelegate>   passthroughDelegate;
    CLLocationManager             * manager;
    
    NSMutableArray                * replayData;
    int                             replayDataOffset;
    NSDate                        * localStartDate;
    NSDate                        * replayStartDate;
    CLLocation                    * replayPreviousLocation;
    BOOL                            replaying;
}

@property (nonatomic, retain) CLLocationManager * manager;
@property (nonatomic, retain) id<CLLocationManagerDelegate> passthroughDelegate;

- (void)startRecording;
- (void)saveRecordingToFile:(NSString*)filepath;
- (void)startReplayingFromFile:(NSString*)filepath;
- (void)stopReplaying;
- (void)queueNextReplayEvent;
- (void)fireNextReplayEvent:(NSTimer *)timer;

#pragma mark CLLocationManagerDelegate Implementation

- (void)locationManager:(CLLocationManager *)manager didUpdateHeading:(CLHeading *)newHeading;
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation;

@end
