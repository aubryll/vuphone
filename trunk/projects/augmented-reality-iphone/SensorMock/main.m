//
//  main.m
//  SensorMock
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SensorMock.h"

int main(int argc, char *argv[]) {
    
    StartSensorMock();
    
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];
    int retVal = UIApplicationMain(argc, argv, nil, nil);
    [pool release];
    return retVal;
}