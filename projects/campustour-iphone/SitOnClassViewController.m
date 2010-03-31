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
	//courses = [[NSArray alloc] initWithObjects:@"CS 250", @"MATH 288", @"ENGM 221", @"CS 101!!!111", nil];
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
		cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:reuseIdentifier] autorelease];
	}
	
	// Configure the cell
	Course *course = [courses objectAtIndex:indexPath.row];
	cell.textLabel.text = course.number;
	cell.detailTextLabel.text = course.title;
	return cell;
}

- (void)dealloc {
    [super dealloc];
}

-(void)loadData:(NSString *)fileName
{
	courses = [CourseXMLReader coursesFromXMLAtPath:@"courses.xml"];
}

@end
