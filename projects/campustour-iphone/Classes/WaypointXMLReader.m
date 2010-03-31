//
//  WaypointImporterXML.m
//  waypoints
//
//  Created by Aaron Thompson on 01/10/10
//

#import "DDXML.h"
#import "DDXMLNode.h"
#import "WaypointXMLReader.h"
#import "Waypoint.h"
#import "XMLReaderUtilities.h"

@interface DDXMLNode (XPathHelpers)

- (DDXMLNode *)nodeForXPath:(NSString *)path error:(NSError **)err;

@end

@implementation DDXMLNode (XPathHelpers)

- (DDXMLNode *)nodeForXPath:(NSString *)path error:(NSError **)err
{
	NSArray *nodes = [self nodesForXPath:path error:err];
	if ([nodes count] == 0) {
		return nil;
	} else {
		return [nodes objectAtIndex:0];
	}
}

@end


@implementation WaypointXMLReader

+ (NSArray *)waypointsFromXMLAtPath:(NSString *)path
{
	NSData *xmlData = [NSData dataWithContentsOfFile:path];
	
	// Parse the request
	NSError *err = nil;
	DDXMLDocument *waypointXml = [[DDXMLDocument alloc] initWithData:xmlData options:0 error:&err];
	if (err) {
		NSLog(@"Error loading %@: %@", path, err);
		[waypointXml release];
		return nil;
	}
	
	// Find the first response
	NSArray *nodes = [waypointXml nodesForXPath:@"/waypoints/waypoint" error:&err];
	NSMutableArray *waypoints = [[[NSMutableArray alloc] init] autorelease];
	
	for (DDXMLNode *node in nodes)
	{
		// Fetch the waypoint from our DB.  If it doesn't exist, create a new one.
		NSString *name = [XMLReaderUtilities getXMLData:node tag:@"location" attribute:@"name"];
		NSString *num = [XMLReaderUtilities getXMLData:node tag:@"location" attribute:@"num"];
		NSString *description = [XMLReaderUtilities getXMLData:node tag:@"description" attribute:nil];
		NSString *longitude = [XMLReaderUtilities getXMLData:node tag:@"coordinate" attribute:@"longitude"];
		NSString *latitude = [XMLReaderUtilities getXMLData:node tag:@"coordinate" attribute:@"latitude"];
		NSString *imageName = [XMLReaderUtilities getXMLData:node tag:@"image" attribute:nil];
		NSString *funFacts = [XMLReaderUtilities getXMLData:node tag:@"funfacts" attribute:nil];
		NSString *testimonials = [XMLReaderUtilities getXMLData:node tag:@"testimonials" attribute:nil];
		NSString *audioPath = [XMLReaderUtilities getXMLData:node tag:@"audiopath" attribute:nil];
		Waypoint *waypoint = [[Waypoint alloc] init];
		waypoint.name = name;
		waypoint.description = description;
		waypoint.num = [num intValue];
		waypoint.image = [UIImage imageWithContentsOfFile:[[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:imageName]];
		[waypoint setLocation:[longitude doubleValue] latitude:[latitude doubleValue]];
		waypoint.funFacts = funFacts;
		waypoint.testimonials = testimonials;
		waypoint.audioFilePath = audioPath;
		[waypoints addObject:waypoint];
		[waypoint release];
	}
	
	[waypointXml release];
	
	return waypoints;
}

@end
