//
//  POIViewController.m
//  CampusMaps
//
//  Created by Demetri Miller on 12/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POIViewController.h"
#import <UIKit/UIStringDrawing.h>
#import <CoreLocation/CoreLocation.h>
#import "VariableHeightCell.h"
#import <QuartzCore/QuartzCore.h>

#define kCellWidth 300.0f

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
	static NSString *identifier = @"identifier";
	static NSString *varHeightIdentifier = @"varHeight";
	
	UITableViewCell *cell;
	VariableHeightCell *varCell;
	
	// Determine which cell to reuse.
	if (indexPath.section == 0) {
		cell = [tableView dequeueReusableCellWithIdentifier:identifier];
	} else {
		cell = [tableView dequeueReusableCellWithIdentifier:varHeightIdentifier];
	}
	
	// Determine which cell type to create if needed.
	if (cell == nil) {
		if (indexPath.section == 0)
			cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 
										   reuseIdentifier:identifier] autorelease];
		else {
			cell = [[[VariableHeightCell alloc] initWithStyle:UITableViewCellStyleDefault 
											  reuseIdentifier:varHeightIdentifier] autorelease];
		}
	}
	
	
	varCell = (VariableHeightCell *)cell;
	UIImageView *poiImage;
	
	UIView *backView;
	
	// Configure the cell.
	switch (indexPath.section) {
		case 0:	// Image
			NSLog(@"loading image…");
			// Backview used to remove white background from image cell.
			backView = [[UIView alloc] initWithFrame:CGRectZero];
			backView.backgroundColor = [UIColor clearColor];
			
			// Setup the image to display prettily.
			poiImage = [[UIImageView alloc] initWithImage:[poi image]];
			
			poiImage.frame = CGRectMake([self getImageOffset:poiImage], 
										0.0, 
										[self getImageWidth:poiImage], 
										[self getImageHeight:poiImage]);
			poiImage.contentMode = UIViewContentModeCenter;
			poiImage.layer.cornerRadius = 10.0f;
			poiImage.clipsToBounds = YES;
			
			[cell.contentView addSubview:poiImage];
			cell.backgroundView = backView;
			[backView release];
			[poiImage release];
			break;
			
		case 1: // Name
			[varCell setText:poi.name];
			break;
			
		case 2: // Distance
			// TODO: Logic for determining distance needs to go here.
			//CLLocation *curLocation;
			[varCell setText:@"NOOB!!!"];
			break;
			
		case 3: // Details
			[varCell setText:poi.details];
			break;
		
		default:
			break;
	}
		
	return cell;
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
			return [poi image].size.height;
		case 1:
		case 2:
		case 3:
			return [(VariableHeightCell *)cell height];
		default:
			return 44.0;
	}
}

- (CGFloat)getImageHeight:(UIImageView *)image 
{
	return image.frame.size.height;
}

- (CGFloat)getImageWidth:(UIImageView *)image
{
	if (image.frame.size.width > kCellWidth) {
		return kCellWidth;
	} else {
		return image.frame.size.width;
	}
}

// Returns the offset needed to evenly space the image
// horizontally.
-(CGFloat) getImageOffset:(UIImageView *) image
{
	return ((kCellWidth - [self getImageWidth:image])/2.0f);
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
