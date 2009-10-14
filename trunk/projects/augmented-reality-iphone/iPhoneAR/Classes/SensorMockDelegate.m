//
//  SensorMockDelegate.m
//  SensorMock
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SensorMockDelegate.h"


@implementation SensorMockDelegate

@synthesize manager;
@synthesize passthroughDelegate;

- (void)startRecording
{
    [replayData release];
    replayData = [[NSMutableArray alloc] init];
    [replayData addObject: [NSDate date]];
}

- (void)saveRecordingToFile:(NSString*)filepath
{
    NSLog(@"Recording saved to %@", filepath);
    [NSKeyedArchiver archiveRootObject:replayData toFile:filepath];
    [replayData release];
    replayData = nil;
}

- (void)startReplayingFromFile:(NSString*)filepath
{
    NSData * data = [NSData dataWithContentsOfFile: filepath];
    replayData = [[NSKeyedUnarchiver unarchiveObjectWithData: data] retain];
    replayStartDate = [replayData objectAtIndex: 0];
    localStartDate = [NSDate new];
    replaying = YES;
    replayDataOffset = 0;
    [self queueNextReplayEvent];
}

- (void)stopReplaying
{
    replaying = NO;
    replayDataOffset = 0;
}


- (void)queueNextReplayEvent
{
    replayDataOffset ++;
    if (replayDataOffset >= [replayData count]){
        [self stopReplaying];
        return;
    }
    // get the timestamp from the next event
    NSDictionary * d = [replayData objectAtIndex: replayDataOffset];
    NSTimeInterval replayTimeIntoReplay = [[d objectForKey: @"timestamp"] timeIntervalSinceDate: replayStartDate];
    NSTimeInterval localTimeIntoReplay = [[NSDate date] timeIntervalSinceDate: localStartDate];
    
    // figure out how long we have to wait
    NSTimeInterval timeToFire = replayTimeIntoReplay - localTimeIntoReplay;
    [NSTimer scheduledTimerWithTimeInterval:timeToFire target:self selector:@selector(fireNextReplayEvent:) userInfo:d repeats:NO];

}

- (void)fireNextReplayEvent:(NSTimer *)timer
{
    NSDictionary * eventData = (NSDictionary*)[timer userInfo];
    
    if (replaying == NO)
        return;
        
    if ([eventData objectForKey:@"location"] != nil){
        CLLocation * location = (CLLocation*)[eventData objectForKey:@"location"];
        if ([passthroughDelegate respondsToSelector:@selector(locationManager:didUpdateToLocation:fromLocation:)])
            [passthroughDelegate locationManager:manager didUpdateToLocation:location fromLocation:replayPreviousLocation];
        [replayPreviousLocation release];
        replayPreviousLocation = [location retain];
    
    } else {
        CLHeading * heading = (CLHeading*)[eventData objectForKey:@"heading"];
        if ([passthroughDelegate respondsToSelector:@selector(locationManager:didUpdateHeading:)])
            [passthroughDelegate locationManager:manager didUpdateHeading: heading];
    }
    
    [self queueNextReplayEvent];
}

#pragma mark CLLocationManagerDelegate Implementation

- (void)locationManager:(CLLocationManager *)m didUpdateHeading:(CLHeading *)newHeading
{
    if (replayData) {
        NSMutableDictionary * d = [NSMutableDictionary dictionary];
        [d setObject:[NSDate date] forKey:@"timestamp"];
        [d setObject: newHeading forKey: @"heading"];
        [replayData addObject: d];
        NSLog(@"Heading reading saved.");
    }
    if ([passthroughDelegate respondsToSelector:@selector(locationManager:didUpdateHeading:)])
        [passthroughDelegate locationManager: m didUpdateHeading: newHeading];
}

- (void)locationManager:(CLLocationManager *)m didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    if (replayData){
        NSMutableDictionary * d = [NSMutableDictionary dictionary];
        [d setObject:[NSDate date] forKey:@"timestamp"];
        [d setObject: newLocation forKey: @"location"];
        [replayData addObject: d];
        NSLog(@"Location reading saved.");
    }
    
    if ([passthroughDelegate respondsToSelector:@selector(locationManager:didUpdateToLocation:fromLocation:)])
        [passthroughDelegate locationManager: m didUpdateToLocation: newLocation fromLocation: oldLocation];
}

@end
