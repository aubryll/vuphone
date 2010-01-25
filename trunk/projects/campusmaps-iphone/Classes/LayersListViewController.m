//
//  LayersListViewController.m
//  CampusMaps
//
//  Created by Aaron Thompson on 12/2/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "LayersListViewController.h"


@implementation LayersListViewController

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
	[super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


#pragma mark UITableViewDelegate, UITableViewDataSource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return 1;
}


// Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return [layers count];
}


// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
	static NSString *CellIdentifier = @"Cell";
	
	UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	if (cell == nil) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
	}
	
	Layer *layer = [layers objectAtIndex:indexPath.row];
	cell.textLabel.text = layer.name;
	
	return cell;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	[delegate layersListViewController:self didChooseLayer:[layers objectAtIndex:indexPath.row]];
	[self dismissModalViewControllerAnimated:YES];
}


- (void)dealloc {
	[super dealloc];
}


@synthesize layers;
@synthesize delegate;

@end
