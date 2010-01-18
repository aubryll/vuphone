//
//  RemoteEventLoader.h
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DDXML.h"
#import "Restaurant.h"
#import "HourRange.h"

@interface RestaurantImporterXML : NSObject {

}

+ (void)getRestaurantsFromXMLAtPath:(NSString *)path intoContext:(NSManagedObjectContext *)context;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoRestaurant:(Restaurant *)restaurant;

@end
