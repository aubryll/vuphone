//
//  SitOnClassViewController.m
//  Campus Tour
//
//  Created by sma1 on 3/23/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "SitOnClassViewController.h"
#import "CourseXMLReader.h"
#import "Course.h"

@implementation SitOnClassViewController 

- (void)viewDidLoad {
	courses = [[NSMutableArray alloc] init];

	NSString *courseXMLPath = [[NSBundle mainBundle] pathForResource:@"courses" ofType:@"xml"];
	courses = [[CourseXMLReader coursesFromXMLAtPath:courseXMLPath] retain];
}

- (void)viewDidUnload {
	[courses release];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
	return [courses count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	static NSString *reuseIdentifier = @"SitInOnClass cell reuse";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:reuseIdentifier];

    if (!cell) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:reuseIdentifier] autorelease];
	}
	
	// Configure the cell
	Course *course = [courses objectAtIndex:indexPath.row];
	cell.textLabel.text = course.title;
	cell.detailTextLabel.text = course.time;

	return cell;
}

- (void)dealloc {
    [super dealloc];
}
@end
