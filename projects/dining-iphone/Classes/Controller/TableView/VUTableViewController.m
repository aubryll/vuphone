#import "VUTableViewController.h"
#import "VUTableViewCellController.h"

@interface VUTableViewController (private)

- (void)constructTableGroups;

@end


@implementation VUTableViewController

- (NSArray *)tableGroups
{
	if (!tableGroups)
	{
		[self constructTableGroups];
	}
	
	return tableGroups;
}

//
// constructTableGroups
//
// Creates/updates cell data. This method should only be invoked directly if
// a "reloadData" needs to be avoided. Otherwise, updateAndReload should be used.
//
- (void)constructTableGroups
{
	tableGroups = [[NSArray arrayWithObject:[NSArray array]] retain];
}

//
// clearTableGroups
//
// Releases the table group data (which will be recreated when next needed)
//
- (void)clearTableGroups
{
	[tableGroups release];
	tableGroups = nil;
}

//
// updateAndReload
//
// Performs all work needed to refresh the data and the associated display
//
- (void)updateAndReload
{
	[self clearTableGroups];
	[self constructTableGroups];
	[self.tableView reloadData];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)aTableView
{
	return [[self tableGroups] count];
}

- (NSInteger)tableView:(UITableView *)aTableView numberOfRowsInSection:(NSInteger)section
{
	id<VUTableViewCellController> controller = [[self tableGroups] objectAtIndex:section];

	return [controller tableView:aTableView numberOfRowsInSection:section];
}

- (NSString *)tableView:(UITableView *)aTableView titleForHeaderInSection:(NSInteger)section {
	NSObject <VUTableViewCellController> *controller = [[self tableGroups] objectAtIndex:section];

	if ([controller respondsToSelector:@selector(tableView:titleForHeaderInSection:)]) {
		return [controller tableView:aTableView titleForHeaderInSection:section];
	} else {
		return nil;
	}
}

- (UITableViewCell *)tableView:(UITableView *)aTableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	id<VUTableViewCellController> controller = [[self tableGroups] objectAtIndex:indexPath.section];
	
	return [controller tableView:aTableView cellForRowAtIndexPath:indexPath];
}

- (void)tableView:(UITableView *)aTableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSObject<VUTableViewCellController> *controller = [tableGroups objectAtIndex:indexPath.section];
	
	if ([controller respondsToSelector:@selector(tableView:didSelectRowAtIndexPath:)])
	{
		[controller tableView:aTableView didSelectRowAtIndexPath:indexPath];
	}
	else
	{
		[aTableView deselectRowAtIndexPath:indexPath animated:NO];
	}

}

- (CGFloat)tableView:(UITableView *)aTableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	NSObject<VUTableViewCellController> *controller = [tableGroups objectAtIndex:indexPath.section];
	
	if ([controller respondsToSelector:@selector(tableView:heightForRowAtIndexPath:)])
	{
		return [controller tableView:aTableView heightForRowAtIndexPath:indexPath];
	}
	
	return 44.0f;
}

/*
#pragma mark UIViewController methods
- (void)viewWillAppear:(BOOL)animated
{
	// Have to propagate this manually to all controllers
	for (NSArray *group in [self tableGroups]) {
		for (NSObject<VUTableViewCellController> *cell in group) {
			[(UIViewController *)cell viewWillAppear:animated];
		}
	}
	
	[super viewWillDisappear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	// Have to propagate this manually to all controllers
	for (NSArray *group in [self tableGroups]) {
		for (NSObject<VUTableViewCellController> *cell in group) {
			[(UIViewController *)cell viewWillDisappear:animated];
		}
	}

	[super viewWillDisappear:animated];
}
*/

//
// didReceiveMemoryWarning
//
// Release any cache data.
//
- (void)didReceiveMemoryWarning
{
	[super didReceiveMemoryWarning];
	
	[self clearTableGroups];
}

//
// dealloc
//
// Release instance memory
//
- (void)dealloc
{
	[self clearTableGroups];
	[super dealloc];
}

@end

