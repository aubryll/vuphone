//
//  EventListViewController.h
//  Events
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>

#import "SourcesViewController.h"
#import "EventViewController.h"
#import "EventListCell.h"
#import "Event.h"

@interface EventListViewController : UITableViewController <NSFetchedResultsControllerDelegate, UISearchBarDelegate, SourcesViewDelegate> {

	NSManagedObjectContext *context;
	NSFetchedResultsController *fetchedResultsC;
	CLLocationManager *locationManager;
	NSPredicate *sourcesPredicate;
	NSPredicate *filterPredicate;
	NSPredicate *datePredicate;
	
	NSArray *sectionIndexTitles;
	NSArray *chosenSources;
	
	EventViewController *eventViewController;
	IBOutlet UIBarButtonItem *addButton;
	IBOutlet UISearchBar *searchBar;
	IBOutlet UISearchDisplayController *searchDisplayController;
	BOOL isSearching;
}

- (IBAction)addEvent:(id)sender;
- (IBAction)showSourcesSheet:(id)sender;
- (EventViewController *)eventViewController;
- (void)filterContentForSearchText:(NSString*)searchText scope:(NSString*)scope;
- (void)setChosenSources:(NSArray *)sources;
- (void)setSourcesPredicate:(NSPredicate *)pred;
- (void)setFilterPredicate:(NSPredicate *)pred;
- (NSPredicate *)predicate;
- (void)refetch;

@property (nonatomic, retain) NSManagedObjectContext *context;
@property (nonatomic, retain) NSFetchedResultsController *fetchedResultsC;
@property (nonatomic, retain) CLLocationManager *locationManager;

@end
