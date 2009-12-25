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
	switch (indexPath.section) {
		case 0:	// Image
			NSLog(@"loading image…");
			[poiImageCell setupImage:[poi image]];
			break;
			
		case 1: // Name
			[varCell setText:poi.name];
			break;
			
		case 2: // Distance			
			[varCell setText:[self distanceToPOI]];
			break;
			
		case 3: // Details
			[varCell setText:poi.details];
			break;
		
		default:
			break;
	}
		
	return cell;
}

// Returns the distance (in miles) formatted as a string.
// TODO: handle mem mgmt in this method.
- (NSString *)distanceToPOI
{
	CLLocation *curLocation = [[CLLocation alloc] init];
	curLocation = [[LocationManagerSingleton sharedManager] lastKnownLocation];
	CLLocation *poiLocation = [[CLLocation alloc] initWithLatitude:[poi.latitude doubleValue] 
														 longitude:[poi.longitude doubleValue]];
	
	// Distance measured in meters. 
	CLLocationDistance distance = [curLocation getDistanceFrom:poiLocation];
	
	// Convert distance to miles.
	distance = distance * 0.000621371192;
	
	// Round off double to scale decimal places.
	NSDecimalNumberHandler *mydnh = [[[NSDecimalNumberHandler alloc] initWithRoundingMode:NSRoundPlain 
																					scale:2
																		 raiseOnExactness:NO 
																		  raiseOnOverflow:NO 
																		 raiseOnUnderflow:NO 
																	  raiseOnDivideByZero:NO] autorelease];
	NSDecimalNumber *myDecimal = [[[NSDecimalNumber alloc] initWithDouble:distance] autorelease];
	NSDecimalNumber *result = [myDecimal decimalNumberByRoundingAccordingToBehavior:mydnh];
	
	return [NSString stringWithFormat:@"%@ miles", result];
}

// Converts a CLLocationDistance to different units based on parameters.
/*
- (CLLocationDistance)convertDistance:(CLLocationDistance)distance toUnits:(units)newUnits
{
	// For now, we can assume the old units will always be meters.
	// 1 meter =
	//			3.2808399 feet
	//			1.0936133 yards
	//			.001 kilometers
	//			0.000621371192 miles
	
	switch (newUnits) {
		case FEET: 
			return (distance*3.2808399);
		case METERS:
			return distance;
		case YARDS: 
			return (distance * 1.0936133);
		case KILOMETERS:
			return (distance * .001);
		case MILES:
			return (distance * .000621371192);
		default:
			break;
	}
}
 */

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
			return [poi image].size.height;
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
