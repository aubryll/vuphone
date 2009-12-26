//
//  POIViewController.m
//  CampusMaps
//
//  Created by Demetri Miller on 12/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POIViewController.h"
#import "VariableHeightCell.h"
#import "POIImageViewCell.h"
#import "LocationManagerSingleton.h"
#import <QuartzCore/QuartzCore.h>
#import <UIKit/UIStringDrawing.h>
#import <CoreLocation/CoreLocation.h>


@implementation POIViewController

@synthesize poi;

// Return the number of sections for the table.
//
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
	// Note that we'll be adding a 4th section for the image later.
	return 4;
}

//
// tableView:numberOfRowsInSection:
//
// Returns the number of rows in a given section.
//
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return 1;
}

//
// tableView:cellForRowAtIndexPath:
//
// Returns the cell for a given indexPath.
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	//static NSString *identifier = @"identifier";
	static NSString *varHeightIdentifier = @"varHeight";
	static NSString *poiImageIdentifier = @"poiImageID";
	
	UITableViewCell *cell;
	VariableHeightCell *varCell;
	POIImageViewCell *poiImageCell;
	
	// Determine which cell to reuse.
	if (indexPath.section == 0) {
		cell = [tableView dequeueReusableCellWithIdentifier:poiImageIdentifier];
	} else {
		cell = [tableView dequeueReusableCellWithIdentifier:varHeightIdentifier];
	}
	
	// Determine which cell type to create if needed.
	if (cell == nil) {
		if (indexPath.section == 0)
			cell = [[[POIImageViewCell alloc] initWithStyle:UITableViewCellStyleDefault
										   reuseIdentifier:poiImageIdentifier] autorelease];
		else {
			cell = [[[VariableHeightCell alloc] initWithStyle:UITableViewCellStyleDefault 
											  reuseIdentifier:varHeightIdentifier] autorelease];
		}
	}
	
	poiImageCell = (POIImageViewCell *)cell;
	varCell = (VariableHeightCell *)cell;
	

	// Configure the cell.
	switch (indexPath.section)
	{
		case 0:	// Image
			NSLog(@"Configuring image cell, imageLoadingState = %i", poi.imageLoadingState);
			if (poi.imageLoadingState == POIImageNotYetLoadingState) {
				
				NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:tableView, @"tableView", 
																					indexPath, @"indexPath", nil];
				// spawn thread to load image.
				[NSThread detachNewThreadSelector:@selector(loadImage:) toTarget:self withObject:params];
			}
			else if (poi.imageLoadingState == POIImageLoadedState) {
				[poiImageCell setupImage:[poi image]];
			}
			
			poiImageCell.imageLoadingState = poi.imageLoadingState;
			break;
			
		case 1: // Name
			[varCell setText:poi.name];
			break;
			
		case 2: // Distance			
			[varCell setText:[poi distanceFromLocation:[[LocationManagerSingleton sharedManager] lastKnownLocation]]];
			break;
			
		case 3: // Details
			if ([poi.details length] == 0) { 
				[varCell setText:@"Details not available."]; 
			} else {
				[varCell setText:poi.details];
			} 
			break;
		default:
			break;
	}
		
	return cell;
}


// Spawn a new thread to load the image associated with selected POI.
- (void)loadImage:(NSDictionary *)params
{
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	
	// Load the image into POI.
	[poi loadImage];

	// To give yourself enough time to see the loading view for debugging, uncomment the following line
//	[NSThread sleepForTimeInterval:5.0f];
	
	// Refresh table view.
	[self performSelectorOnMainThread:@selector(reloadTableView:) withObject:params waitUntilDone:YES];

	[pool release];
}

- (void)reloadTableView:(NSDictionary *)params
{
	[[params objectForKey:@"tableView"] reloadRowsAtIndexPaths:[NSArray arrayWithObject:[params objectForKey:@"indexPath"]]
											  withRowAnimation:UITableViewRowAnimationFade];	
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section 
{
	switch (section) {
		case 1:
			return @"Name";
		case 2:
			return @"Distance";
		case 3:
			return @"Details";
		default:
			return nil;
	}
}
- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	UITableViewCell *cell = [self tableView:aTableView cellForRowAtIndexPath:indexPath];
	switch (indexPath.section) {
		case 0:
			return [(POIImageViewCell *)cell height];
		case 1:
		case 2:
		case 3:
			return [(VariableHeightCell *)cell height];
		default:
			return 44.0;
	}
}

//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[tableView deselectRowAtIndexPath:indexPath	animated:NO];
}


- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}


@end
