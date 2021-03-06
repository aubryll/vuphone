//
//  RestaurantViewController.m
//  Dining
//
//  Created by Aaron Thompson on 1/13/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "RestaurantViewController.h"
#import "HourRange.h"
#import "DiningAppDelegate.h"
#import "ImageCellController.h"
#import "SectionTitleAndButtonCellController.h"
#import "VariableHeightCellController.h"
#import "HoursCellController.h"

@implementation RestaurantViewController

@synthesize restaurant;

- (void)viewDidLoad {
	menuItemsLoaded = NO;
}

- (void)viewWillAppear:(BOOL)animated
{
	[super viewWillAppear:animated];
	
	self.navigationItem.title = restaurant.name;
}


- (void)constructTableGroups
{
	if (tableGroups) {
		[tableGroups release];
		tableGroups = nil;
	}
	
	NSMutableArray *mTableGroups = [[NSMutableArray alloc] init];
	
	// Image
	if ([restaurant image] != nil) {
		ImageCellController *icc = [[ImageCellController alloc] init];
		icc.image = [restaurant image];
		[mTableGroups addObject:icc];
		[icc release];
	}
	
	// Type
	SectionTitleAndButtonCellController *stabcc = [[SectionTitleAndButtonCellController alloc] init];
	stabcc.sectionTitle = [@"Type: " stringByAppendingString:restaurant.type];
	stabcc.buttonTarget = nil;
	[mTableGroups addObject:stabcc];
	[stabcc release];
	
	// Distance + Show on Map
	stabcc = [[SectionTitleAndButtonCellController alloc] init];
	stabcc.sectionTitle = [NSString stringWithFormat:@"Distance: %@", [restaurant distanceAsString]];
	stabcc.buttonTitle = @"Show on Map";
	stabcc.buttonSelector = @selector(showRestaurantOnMap:);
	stabcc.buttonTarget = [[UIApplication sharedApplication] delegate];
	stabcc.buttonObject = restaurant;
	[mTableGroups addObject:stabcc];
	[stabcc release];
	
	// Hours
	HoursCellController *hcc = [[HoursCellController alloc] init];
	hcc.sectionTitle = @"Hours";
	hcc.hours = [restaurant groupedOpenHours];
	[mTableGroups addObject:hcc];
	[hcc release];
	
	if (restaurant.details != nil) {
		// Details
		VariableHeightCellController *vhcc = [[VariableHeightCellController alloc] init];
		vhcc.sectionTitle = @"Details";
		vhcc.contentStrings = [NSArray arrayWithObject:restaurant.details];
		[mTableGroups addObject:vhcc];
		[vhcc release];
	}
	
	// Phone Number
	if (restaurant.phone != nil) {
		SectionTitleAndButtonCellController *stabcc = [[SectionTitleAndButtonCellController alloc] init];
		stabcc.sectionTitle = @"Phone";
		stabcc.buttonTitle = restaurant.phone;
		NSURL *phoneUrl = [NSURL URLWithString:[@"tel://" stringByAppendingString:restaurant.phone]];
		if ([[UIApplication sharedApplication] canOpenURL:phoneUrl]) {
			stabcc.buttonSelector = @selector(openURL:);
			stabcc.buttonTarget = [UIApplication sharedApplication];
			stabcc.buttonObject = phoneUrl;
		}

		[mTableGroups addObject:stabcc];
		[stabcc release];
	}
	
	// Menu
	if (restaurant.websiteLocationNumber != nil) {
		VariableHeightCellController *vhcc = [[VariableHeightCellController alloc] init];
		vhcc.sectionTitle = @"Menu";
		if (menuItemsLoaded) {
			vhcc.contentStrings = [restaurant menuItems];
		} else {
			vhcc.contentStrings = [NSArray arrayWithObject:@"Loading…"];
			[NSThread detachNewThreadSelector:@selector(loadMenuItems:) toTarget:self withObject:nil];
		}
		
		[mTableGroups addObject:vhcc];
		[vhcc release];
	}
	
	// URL
	if (restaurant.urlString != nil) {
		SectionTitleAndButtonCellController *stabcc = [[SectionTitleAndButtonCellController alloc] init];
		stabcc.sectionTitle = @"Website";
		stabcc.buttonTitle = restaurant.urlString;
		NSURL *webUrl = [NSURL URLWithString:restaurant.urlString];
		if ([[UIApplication sharedApplication] canOpenURL:webUrl]) {
			stabcc.buttonSelector = @selector(openURL:);
			stabcc.buttonTarget = [UIApplication sharedApplication];
			stabcc.buttonObject = webUrl;
		}
		
		[mTableGroups addObject:stabcc];
		[stabcc release];
		
	}
	
	tableGroups = mTableGroups;
}

- (void)loadMenuItems:(id)object {
	NSAutoreleasePool *localPool = [[NSAutoreleasePool alloc] init];

	// The restaurant lazy-loads its menu items, so just asking it for them here
	[restaurant menuItems];
	menuItemsLoaded = YES;
	[self performSelectorOnMainThread:@selector(updateAndReload) withObject:nil waitUntilDone:NO];
	
	[localPool release];
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
	[super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}


- (void)dealloc {
	self.restaurant = nil;

	[super dealloc];
}


@end
