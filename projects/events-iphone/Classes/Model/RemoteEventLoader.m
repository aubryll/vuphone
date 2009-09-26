//
//  RemoteEventLoader.m
//  VandyUpon
//
//  Created by Aaron Thompson on 9/7/09.
//
/* Sample XML server response:
 <eventrequestresponse>
	 <event>
		 <name>Test</name>
		 <loc>
			<lat>36.1437</lat>
			<lon>-86.8046</lon>
		 </loc>
		 <owner>true</owner>
		 <start>1248290654565</start>
		 <end>1248291254565</end>
		 <eventid>1</eventid>
		 <lastupdate>1248291254565</lastupdate>
	 </event>
 </eventrequestresponse>
 
 */

#define SAMPLE_EVENT_REQUEST_RESPONSE

#import "RemoteEventLoader.h"
#import "EntityConstants.h"

@implementation RemoteEventLoader

+ (NSArray *)eventsFromServerWithContext:(NSManagedObjectContext *)context
{
	return [RemoteEventLoader eventsFromServerSince:nil withContext:context];
}

+ (NSArray *)eventsFromServerSince:(NSDate *)date withContext:(NSManagedObjectContext *)context
{	// Format the url string
#ifndef SAMPLE_EVENT_REQUEST_RESPONSE
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	NSMutableString *urlString = [NSMutableString stringWithString:@"http://afrl-gift.dre.vanderbilt.edu:8080/vandyupon/events"];
	[urlString appendString:@"?type=eventrequest"];
	[urlString appendFormat:@"&lat=%f", CAMPUS_CENTER_LATITUDE];
	[urlString appendFormat:@"&lon=%f", CAMPUS_CENTER_LONGITUDE];
	[urlString appendFormat:@"&updatetime=%@", (date == nil) ? @"-1" : [dateFormatter stringFromDate:date]];
	[urlString appendFormat:@"&dist=%i", -1];
	[urlString appendFormat:@"&userid=%@", [[UIDevice currentDevice] uniqueIdentifier]];
	[urlString appendString:@"&resp=xml"];
	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSLog(escapedUrlString);
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];
	// Make the request to get the data
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];
#else
	NSData *responseData = [NSData dataWithContentsOfFile:@"/Users/thompsonaaron/PROGRAMMING/VUPhone/trunk/projects/events-iphone/sampleEventRequestResponse.xml"];
#endif

	// Parse the request
	NSError *err = nil;
	DDXMLDocument *responseXml = [[DDXMLDocument alloc] initWithData:responseData options:0 error:&err];
	NSLog(@"Error loading response XML: %@", err);
	
	// Find the first response
	NSArray *nodes = [responseXml nodesForXPath:@"./eventrequestresponse/event" error:&err];
	NSMutableArray *events = [[NSMutableArray alloc] initWithCapacity:[nodes count]];
	if ([nodes count] > 0)
	{
		for (DDXMLNode *node in nodes)
		{
			// Create an event and load data into it
			Event *event = [NSEntityDescription insertNewObjectForEntityForName:VUEntityNameEvent
														 inManagedObjectContext:context];
			[RemoteEventLoader getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event];
			[events addObject:event];
		}
		[context save:&err];
	}
	else
	{
		NSLog(@"No nodes found: %@", responseXml);
	}

	return events;
}

+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event
{
	DDXMLNode *prop;
	NSError *err;
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./name" error:&err] objectAtIndex:0];
	event.name = [prop stringValue];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./loc/lat" error:&err] objectAtIndex:0];
//	event.location.lat = [prop stringValue];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./loc/lng" error:&err] objectAtIndex:0];
//	event.location.lng = [prop stringValue];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./start" error:&err] objectAtIndex:0];
	event.startTime = [dateFormatter dateFromString:[prop stringValue]];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./end" error:&err] objectAtIndex:0];
	event.endTime = [dateFormatter dateFromString:[prop stringValue]];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./eventid" error:&err] objectAtIndex:0];
//	event.eventId = [prop stringValue];
}

@end
