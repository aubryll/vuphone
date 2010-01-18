//
//  KindSortHelper.h
//  Dining
//
//  Created by Aaron Thompson on 1/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RestaurantListSortHelper.h"

@interface TypeSortHelper : NSObject <RestaurantListSortHelper> {
	NSArray *restaurants;
	NSArray *_sortDescriptors;
	NSArray *_sections;
}

@property (retain) NSArray *restaurants;

- (NSArray *)sections;

@end
