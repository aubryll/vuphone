//
//  RestaurantListSortHelper.h
//  Dining
//
//  Created by Aaron Thompson on 1/16/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RestaurantListCell.h"
#import "Restaurant.h"

@protocol RestaurantListSortHelper

- (id)initWithRestaurants:(NSArray *)restaurantsArray;
- (NSArray *)sortDescriptors;
- (NSInteger)numberOfSections;
- (NSInteger)numberOfRowsInSection:(NSInteger)section;
- (NSString *)titleForHeaderInSection:(NSInteger)section;
- (void)configureCell:(RestaurantListCell *)cell atIndexPath:(NSIndexPath *)indexPath;
- (Restaurant *)restaurantAtIndexPath:(NSIndexPath *)indexPath;


@end
