//
//  POIViewController.m
//  CampusMaps
//
//  Created by Demetri Miller on 12/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "POIViewController.h"
#import <UIKit/UIStringDrawing.h>



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
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
	
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:identifier] autorelease];
	}
	
	CGRect contentRect = CGRectMake(10.0, 0.0, 280, 56);
	UITextView *textLabel = [[UITextView alloc] initWithFrame:contentRect];
	textLabel.editable = NO;
	textLabel.font = [UIFont systemFontOfSize:13.0];
	[cell.contentView addSubview:textLabel];
	
	// Configure the cell.
	switch (indexPath.section) {
		case 0:
			textLabel.text = poi.name;
			break;
		case 1:
			// TODO: Logic for determining distance needs to go here.
			textLabel.text = @"NOOB!!!";
			break;
		case 2:
			textLabel.text = poi.details;
			break;
		case 3:
			// Release the current subview and then add the poi image. 
			[cell.contentView release];
			UIImageView *poiImage = [[UIImageView alloc] initWithImage:[poi image]];
			[cell.contentView addSubview:poiImage];
			break;
		default:
			break;
	}
	
	[textLabel release];
	
	return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section 
{
	switch (section) {
		case 0:
			return @"Name";
		case 1:
			return @"Distance";
		case 2:
			return @"Details";
		case 3:
			return @"Image";
		default:
			return nil;
	}
}
- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{

	switch (indexPath.section) {
		case 0:
			return 30.0;
		case 1:
			return 30.0;
		case 2:
			return [self getSizeOfText:poi.details].height;
		case 3:
			return [poi image].size.height;
		default:
			return 44.0;
	}
}

// Helper method used to calculate the frame for the table view cell.
-(CGSize)getSizeOfText:(NSString *)text {
	return [text sizeWithFont:[UIFont systemFontOfSize:13.0] 
			constrainedToSize:CGSizeMake(130.0, 56.0) lineBreakMode:UILineBreakModeWordWrap];
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
