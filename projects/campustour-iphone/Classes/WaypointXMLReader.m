//
//  WaypointImporterXML.m
//  waypoints
//
//  Created by Aaron Thompson on 01/10/10
//

#import "WaypointXMLReader.h"
#import "Waypoint.h"

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
		NSString *name = [WaypointXMLReader getXMLData:node tag:@"location" attribute:@"name"];
		NSString *num = [WaypointXMLReader getXMLData:node tag:@"location" attribute:@"num"];
		NSString *description = [WaypointXMLReader getXMLData:node tag:@"description" attribute:nil];
		NSString *longitude = [WaypointXMLReader getXMLData:node tag:@"coordinate" attribute:@"longitude"];
		NSString *latitude = [WaypointXMLReader getXMLData:node tag:@"coordinate" attribute:@"latitude"];
		NSString *imageName = [WaypointXMLReader getXMLData:node tag:@"image" attribute:nil];
		NSString *funFacts = [WaypointXMLReader getXMLData:node tag:@"funfacts" attribute:nil];
		NSString *testimonials = [WaypointXMLReader getXMLData:node tag:@"testimonials" attribute:nil];
		NSString *audioPath = [WaypointXMLReader getXMLData:node tag:@"audiopath" attribute:nil];
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
	}
	
	[waypointXml release];
	
	return waypoints;
}


// Currently doesn't support xpath returning multiple nodes, but could be reworked to add index as an additional argument
+ (NSString *)getXMLData:(DDXMLNode *)node tag:(NSString *)tagName attribute:(NSString*)attr
{
	NSString *xpathString;
	if (attr != nil)
	{
		xpathString = [NSString stringWithFormat:@"./%@/@%@", tagName, attr];
	}
	else 
	{
		xpathString = [NSString stringWithFormat:@"./%@", tagName];
	}
    
	NSError *err = nil;
	NSArray *tmpArray = [node nodesForXPath:xpathString error:&err];
	if (err)
	{
		NSLog(@"Error retrieving %@: %@", xpathString, err);
		return nil;
	}
	else if ([tmpArray count] == 0)
	{
		NSLog(@"Error the xpath (%@) returned an empty set", tagName);
		return nil;
	}
	else 
	{
		return [(DDXMLNode *) [tmpArray objectAtIndex:0] stringValue];
	}
}

@end
