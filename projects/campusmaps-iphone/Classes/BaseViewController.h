//
//  BaseView.h
//  CampusMaps
//
//  Created by Joshua Stein on 10/15/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MapViewController.h"


@interface BaseViewController : UIViewController {

	IBOutlet MapViewController *mapViewController;
	
	NSManagedObjectContext *managedObjectContext;	    

}

@property (retain) NSManagedObjectContext *managedObjectContext;

@end
