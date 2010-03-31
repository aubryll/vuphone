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
	
	Course *course = [[Course alloc] init];
	course.subject = @"Computer Science 101";
	course.title = @"Intro to Computer Science";
	course.time = @"MWF 12:10-1:00";
	course.desc = @"This is the course description from the course catalog";
	course.place = @"Featheringill 134";
	[courses addObject:course];
	[course release];
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
	static NSString *reuseIdentifier = @"sitInOnClass cell reuse";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:reuseIdentifier];


    if (!cell) {
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:reuseIdentifier] autorelease];
	}
	
	// Configure the cell
	Course *course = [courses objectAtIndex:indexPath.row];
	cell.textLabel.text = course.subject;
	cell.detailTextLabel.text = course.time;

	return cell;
}

- (void)dealloc {
    [super dealloc];
}

- (void)loadData:(NSString *)fileName
{
//	courses = [CourseXMLReader coursesFromXMLAtPath:@"courses.xml"];
}

@end
