//
//  DistanceSortHelper.h
//  Dining
//
//  Created by Aaron Thompson on 1/17/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RestaurantListSortHelper.h"


@interface DistanceSortHelper : NSObject <RestaurantListSortHelper> {
	NSArray *restaurants;
	NSArray *_sortDescriptors;
}
	
@property (retain) NSArray *restaurants;
	
@end
