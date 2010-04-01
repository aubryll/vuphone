//
//  VUEventTestCase.m
//  Commencement
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <SenTestingKit/SenTestingKit.h>
#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "RemoteEventLoader.h"
#import "Commencementtore.h"

@interface VUEventTests : SenTestCase {
}

@end

@implementation VUEventTests

/* The setUp method is called automatically before each test-case method (methods whose name starts with 'test').
 */
- (void)setUp {
}

/* The tearDown method is called automatically after each test-case method (methods whose name starts with 'test').
 */
- (void)tearDown {
}

- (void)testCommencementFromServer
{
	NSManagedObjectContext *context = [[Commencementtore sharedCommencementtore] sharedContext];
	NSLog(@"Context: %@", context);
	NSArray *Commencement = [RemoteEventLoader CommencementFromServerWithContext:context];
	STAssertNotNil(Commencement, @"Commencement not nil");
	NSLog(@"Commencement: %@", Commencement);
	STAssertEquals([Commencement count], 2, @"Doesn't have 2 Commencement");
}

@end
