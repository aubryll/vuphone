//
//  RestaurantViewController.h
//  Dining
//
//  Created by Aaron Thompson on 1/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Restaurant.h"
#import "VUTableViewController.h"

@interface RestaurantViewController : VUTableViewController {
	Restaurant *restaurant;
    
    BOOL menuItemsLoaded;
}

@property (retain) Restaurant *restaurant;

@end
