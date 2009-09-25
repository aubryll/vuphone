//
//  EventListViewController.h
//  VandyUpon
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

#import "EventViewController.h"
#import "Event.h"

@interface EventListViewController : UITableViewController <CLLocationManagerDelegate, NSFetchedResultsControllerDelegate, UISearchBarDelegate> {

	NSManagedObjectContext *context;
	NSFetchedResultsController *fetchedResultsC;
	CLLocationManager *locationManager;
	
	IBOutlet EventViewController *eventViewController;
	IBOutlet UIBarButtonItem *addButton;
	IBOutlet UISearchBar *searchBar;
}

- (IBAction)addEvent:(id)sender;
- (void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;
- (EventViewController *)eventViewController;

@property (nonatomic, retain) NSManagedObjectContext *context;
@property (nonatomic, retain) NSFetchedResultsController *fetchedResultsC;
@property (nonatomic, retain) CLLocationManager *locationManager;

@end
