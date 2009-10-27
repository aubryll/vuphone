//
//  SourcesViewController.m
//  Events
//
//  Created by Aaron Thompson on 10/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "SourcesViewController.h"
#import "VUSourceTableViewCell.h"

@implementation SourcesViewController

- (IBAction)dismissSheet:(id)sender
{
	[delegate sourcesViewController:self didDismissWithChoices:[chosenSources allObjects]];
	[self dismissModalViewControllerAnimated:YES];
}


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
	
	if (sourceCells == nil)
	{
		NSMutableArray *cells = [[NSMutableArray alloc] initWithCapacity:[sources count]];
		NSString *cellIdentifier = @"sourceCell";
		
		for (int i=0; i<[sources count]; i++)
		{
			VUSourceTableViewCell *cell = [[VUSourceTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
			
			// Set up the cell
			cell.source = [sources objectAtIndex:i];
			[cell setChecked:[chosenSources containsObject:cell.source]];

			[cells addObject:cell];
			[cell release];
		}
		sourceCells = cells;
	}
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
	[sourceCells release];
}


#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [sources count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    return [sourceCells objectAtIndex:indexPath.row];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	[self toggleSelectionForSourceCell:[sourceCells objectAtIndex:indexPath.row]];

	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (void)toggleSelectionForSourceCell:(VUSourceTableViewCell *)cell
{
	// If the cell is already checked
	if ([chosenSources containsObject:cell.source]) {
		// Uncheck it and remove it from the chosen sources
		[cell setChecked:NO];
		[chosenSources removeObject:cell.source];
	} else {
		[cell setChecked:YES];
		[chosenSources addObject:cell.source];
	}
}

- (IBAction)toggleAllSources:(id)sender
{
	// First, check to see if they are all checked
	BOOL allChecked = [chosenSources count] == [sources count];
	
	if (allChecked)
	{
		// Deselect all
		for (VUSourceTableViewCell *cell in sourceCells) {
			[self toggleSelectionForSourceCell:cell];
		}
	}
	else
	{
		// Select all
		for (VUSourceTableViewCell *cell in sourceCells) {
			if (!cell.isChecked) {
				[self toggleSelectionForSourceCell:cell];
			}
		}
	}
}

- (void)dealloc {
    [super dealloc];
	[sourceCells release];
}

@synthesize sources;
@synthesize chosenSources;
@synthesize delegate;

@end

