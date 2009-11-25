//
//  EventRatingsCellController.m
//  Events
//
//  Created by Aaron Thompson on 21/9/09.
//  Copyright 2009 Iostudio, LLC. All rights reserved.
//

#import "EventRatingsCellController.h"
#import "LocationListViewController.h"
#import "VUEditableCell.h"

#define StarImageXOffset 94.0f
#define StarImageSpacing 0.0f
#define StarImageYOffset 14.0f
#define CommentsLabelXOffset 4.0f
#define CommentsLabelYOffset 7.0f
#define CommentsLabelWidth 120.0f
#define CommentsLabelHeight 32.0f

@implementation EventRatingsCellController

- (id)initWithNibName:(NSString *)name bundle:bundle
{
	self = [super initWithNibName:name bundle:bundle];
	if (self != nil) {
		NSString *path = [[NSBundle mainBundle] pathForResource:@"star" ofType:@"png" inDirectory:@"Icons"];
		starImage = [[UIImage alloc] initWithContentsOfFile:path];
	}
	return self;
}


//
// tableView:didSelectRowAtIndexPath:
//
// Handle row selection
//
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
/*	UITableViewController *tvc = (UITableViewController *)tableView.dataSource;

	// Push a EventRatingsViewController
	EventRatingsViewController *controller = [[EventRatingsViewController alloc] initWithNibName:@"EventRatingsView" bundle:nil];
	controller.ratings = self.ratings;
	[tvc.navigationController pushViewController:controller animated:YES];
	[controller release];
*/
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

//
// tableView:cellForRowAtIndexPath:
//
// Returns the cell for a given indexPath.
//
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	// Set up the cell
	static NSString *identifier = @"rating";
	
	UITableViewCell *cell;
	
	cell = [tableView dequeueReusableCellWithIdentifier:identifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue2 reuseIdentifier:identifier] autorelease];
		for (int i=0; i<5; i++) {
			UIImageView *imageView = [[UIImageView alloc] initWithImage:starImage];
			imageView.frame = CGRectMake(StarImageXOffset + (StarImageSpacing+starImage.size.width)*i, StarImageYOffset, starImage.size.width, starImage.size.height);
			[cell addSubview:imageView];
			[imageView release];
		}
	}
	
	cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
	cell.textLabel.text = @"Rating";
	
	// Set up my own detailTextLabel
	CGRect frame = CGRectMake(4.0 + StarImageXOffset + (StarImageSpacing+starImage.size.width)*5,
							  7.0,
							  120.0,
							  32.0);
	UILabel *myDetailTextLabel = [[UILabel alloc] initWithFrame:frame];
	myDetailTextLabel.font = [UIFont systemFontOfSize:14.0f];
	myDetailTextLabel.text = [NSString stringWithFormat:@"(%i comments)", [ratings count]];
	[cell addSubview:myDetailTextLabel];
	[myDetailTextLabel release];
	
	return cell;
}

- (void)dealloc {
	[ratings release];
	[starImage release];
    [super dealloc];
}

@synthesize ratings;

@end
