//
//  SensorMock.h
//  SensorMock
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

@interface CLLocationManager (SensorMock)

- (void)setDelegateSM:(id<CLLocationManagerDelegate>)d;
- (void)startReplayingFromFile:(NSString*)filepath;
- (void)stopReplaying;
- (void)beginRecording;
- (NSMutableArray*)getRecording;
- (void)saveRecordingToFile:(NSString*)filepath;

@end

void StartSensorMock();
void class_setInstanceDoubleVariable(Class class, id objPointer, const char * name, double value);
