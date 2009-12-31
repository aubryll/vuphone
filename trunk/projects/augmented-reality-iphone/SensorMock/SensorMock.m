//
//  SensorMock.m
//  SensorMock
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SensorMock.h"
#import "SensorMockDelegate.h"
#import "MethodSwizzle.h"
#import "UIMotionEvent.h"
#import <objc/runtime.h>

static SensorMockDelegate * _sensorMockDelegate = NO;

@implementation CLLocationManager (SensorMock)

- (void)setDelegateSM:(id<CLLocationManagerDelegate>)d
{
    NSLog(@"Set Delegate SM");
    [self setDelegateSM: _sensorMockDelegate];
    [_sensorMockDelegate setManager: self];
    [_sensorMockDelegate setPassthroughDelegate: d];
}

- (void)startReplayingFromFile:(NSString*)filepath
{
    [_sensorMockDelegate startReplayingFromFile: filepath];
}

- (void)stopReplaying
{
    [_sensorMockDelegate stopReplaying];
}

- (void)beginRecording
{
    [_sensorMockDelegate startRecording];
}

- (NSMutableArray*)getRecording
{
    return [_sensorMockDelegate replayData];
}

- (void)saveRecordingToFile:(NSString*)filepath
{
    [_sensorMockDelegate saveRecordingToFile:filepath];
}

@end

void StartSensorMock() 
{
    _sensorMockDelegate = [[SensorMockDelegate alloc] init];
    MethodSwizzle([CLLocationManager class], @selector(setDelegate:), @selector(setDelegateSM:));
}

void class_setInstanceDoubleVariable(Class class, id objPointer, const char * name, double value) {
    Ivar var;
    if ((var = class_getInstanceVariable(class, name))){
        double * varIndex = (double *)(objPointer + ivar_getOffset(var));
        *varIndex = value;
    }
}
