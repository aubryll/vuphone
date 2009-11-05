//
//  BaseView.h
//  CampusMaps
//
//  Created by Joshua Stein on 10/15/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MapViewController.h"
#import "ListViewController.h"


@interface BaseViewController : UIViewController {

	IBOutlet MapViewController *mapViewController;
	IBOutlet ListViewController *listViewController;
	
	UIButton * flipViewButton;
	BOOL mapViewRunning;
	
	NSManagedObjectContext *managedObjectContext;	    

}

-(void)instantiateFlipViewButton;

@property (retain) NSManagedObjectContext *managedObjectContext;

@end
