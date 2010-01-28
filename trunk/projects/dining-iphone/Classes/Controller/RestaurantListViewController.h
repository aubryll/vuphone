//
//  RestaurantListViewController.h
//  Dining
//
//  Created by Aaron Thompson on 1/11/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
	SortIndexName = 0,
	SortIndexType = 1,
	SortIndexMinutesUntilClose = 2,
	SortIndexDistance = 3
} SortHelperIndex;


@interface RestaurantListViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {

	IBOutlet UITableView *tableView;
	
	NSManagedObjectContext *context;
	NSMutableArray *restaurants;
	NSArray *sortHelpers;
	SortHelperIndex currentSortHelperIndex;
}

@property (retain) NSMutableArray *restaurants;

- (void)initSortHelpers;
- (IBAction)sortChanged:(id)sender;
- (void)setSort:(SortHelperIndex)index;

@end
