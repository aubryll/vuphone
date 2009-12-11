//
//  LayersListViewController.h
//  CampusMaps
//
//  Created by Aaron Thompson on 12/2/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "Layer.h"

@class LayersListViewController;

@protocol LayersListViewDelegate

- (void)layersListViewController:(LayersListViewController *)layersListVC
				  didChooseLayer:(Layer *)layer;

@end


@interface LayersListViewController : UIViewController <UITableViewDataSource, UITableViewDelegate> {
	NSArray *layers;
	id<LayersListViewDelegate> delegate;
}

@property (retain) NSArray *layers;
@property (retain) id<LayersListViewDelegate> delegate;

@end
