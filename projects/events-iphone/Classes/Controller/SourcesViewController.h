//
//  SourcesViewController.h
//  Events
//
//  Created by Aaron Thompson on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VUSourceTableViewCell.h"

@class SourcesViewController;

@protocol SourcesViewDelegate

- (void)sourcesViewController:(SourcesViewController *)sourcesVC
		didDismissWithChoices:(NSSet *)choices;

@end


@interface SourcesViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	IBOutlet UITableView *myTableView;
	id<SourcesViewDelegate> delegate;

	NSArray *sources;
	NSArray *sourceCells;
	NSMutableSet *chosenSources;
}

- (IBAction)dismissSheet:(id)sender;
- (void)toggleSelectionForSourceCell:(VUSourceTableViewCell *)cell;
- (IBAction)toggleAllSources:(id)sender;

@property (retain) NSArray *sources;
@property (retain) NSMutableSet *chosenSources;
@property (retain) id<SourcesViewDelegate> delegate;

@end
