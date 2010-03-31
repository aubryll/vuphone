//
//  CourseXMLReader.m
//  Campus Tour
//
//  Created by sma1 on 3/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "CourseXMLReader.h"
#import "XMLReaderUtilities.h"
#import "Course.h"

@implementation CourseXMLReader


+ (NSArray *)coursesFromXMLAtPath:(NSString *)path
{
	NSData *xmlData = [NSData dataWithContentsOfFile:path];
	
	// Parse the request
	NSError *err = nil;
	DDXMLDocument *coursesXml = [[DDXMLDocument alloc] initWithData:xmlData options:0 error:&err];
	if (err) {
		NSLog(@"Error loading %@: %@", path, err);
		[coursesXml release];
		return nil;
	}
	
	// Find the first response
	NSArray *nodes = [coursesXml nodesForXPath:@"/courses/course" error:&err];
	NSMutableArray *courses = [[[NSMutableArray alloc] init] autorelease];
	
	for (DDXMLNode *node in nodes)
	{
		// Fetch the waypoint from our DB.  If it doesn't exist, create a new one.
		NSString *number = [XMLReaderUtilities getXMLData:node tag:@"information" attribute:@"number"];
		NSString *title = [XMLReaderUtilities getXMLData:node tag:@"information" attribute:@"title"];
		NSString *time = [XMLReaderUtilities getXMLData:node tag:@"information" attribute:@"time"];
		NSString *place = [XMLReaderUtilities getXMLData:node tag:@"information" attribute:@"place"];
		Course *course = [[Course alloc] init];
		course.number = number;
		course.title = title;
		course.time = time;
		course.place = place;
		[courses addObject:course];
	}
	
	[coursesXml release];
	
	return courses;
}



@end
