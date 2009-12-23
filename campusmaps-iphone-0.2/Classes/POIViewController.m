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
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
	VariableHeightCell *varCell;
	CGRect contentRect;
	
	if (cell == nil) {
		if (indexPath.section == 0)
			cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 
										   reuseIdentifier:identifier] autorelease];
		else {
			cell = [[[VariableHeightCell alloc] initWithStyle:UITableViewCellStyleDefault 
											  reuseIdentifier:varHeightIdentifier] autorelease];
			varCell = (VariableHeightCell *)cell;
			contentRect = varCell.textView.frame;
		}
	}
	
	// Configure the cell.
	switch (indexPath.section) {
		case 0:
			// Release the current subview and then add the poi image. 
//			[cell.contentView release];
//			UIImageView *poiImage = [[UIImageView alloc] initWithImage:[poi image]];
//			[cell.contentView addSubview:poiImage];
			break;
		case 1: // Name
			[varCell setText:poi.name];
			break;
		case 2:
			// TODO: Logic for determining distance needs to go here.
			//CLLocation *curLocation;
//			[varCell setText:@"NOOB!!!"];
			break;
		case 3:
			[varCell setText:poi.details];
			break;
		
		default:
			break;
	}
	
	//[varCell release];
	
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
		case 3:
			return ((VariableHeightCell *)cell).textView.contentSize.height + 4.0;
		case 2:
			return 30.0;
		default:
			return 44.0;
	}
}

// Helper method used to calculate the frame for the table view cell.
-(CGSize)getSizeOfText:(NSString *)text {
	return [text sizeWithFont:[UIFont systemFontOfSize:13.0] 
			constrainedToSize:CGSizeMake(20.0, 56.0) lineBreakMode:UILineBreakModeWordWrap];
}

//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

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
