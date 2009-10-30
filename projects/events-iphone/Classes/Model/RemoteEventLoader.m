//
//  RemoteEventLoader.m
//  Events
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

//#define SAMPLE_EVENT_REQUEST_RESPONSE 1

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
	NSMutableString *urlString = [NSMutableString stringWithString:EVENT_REQUEST_URL_STRING];
	[urlString appendString:@"?type=eventrequest"];
	[urlString appendFormat:@"&lat=%f", CAMPUS_CENTER_LATITUDE];
	[urlString appendFormat:@"&lon=%f", CAMPUS_CENTER_LONGITUDE];
	[urlString appendFormat:@"&updatetime=%@", (date == nil) ? @"0" : [dateFormatter stringFromDate:date]];
	[urlString appendFormat:@"&dist=%i", 100000];	// distance is measured in meters
	[urlString appendFormat:@"&userid=%@", [[UIDevice currentDevice] uniqueIdentifier]];
	[urlString appendString:@"&resp=xml"];
	
	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];
	// Make the request to get the data
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];
#else
	NSData *responseData = [NSData dataWithContentsOfFile:@"/Users/thompsonaaron/PROGRAMMING/VUPhone/trunk/projects/events-iphone/sampleEventRequestResponse.xml"];
#endif
	
	[dateFormatter release];

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
	
	[responseXml release];

	return [events autorelease];
}

+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event
{
	DDXMLNode *prop;
	NSError *err;
	NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./name" error:&err] objectAtIndex:0];
	event.name = [prop stringValue];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./loc/lat" error:&err] objectAtIndex:0];
	event.location.latitude = [NSDecimalNumber decimalNumberWithString:[prop stringValue]];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./loc/lng" error:&err] objectAtIndex:0];
	event.location.longitude = [NSDecimalNumber decimalNumberWithString:[prop stringValue]];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./start" error:&err] objectAtIndex:0];
	event.startTime = [dateFormatter dateFromString:[prop stringValue]];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./end" error:&err] objectAtIndex:0];
	event.endTime = [dateFormatter dateFromString:[prop stringValue]];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./eventid" error:&err] objectAtIndex:0];
	event.serverId = [prop stringValue];
	
	[dateFormatter release];
}

+ (void)submitEvent:(Event *)event
{
	NSMutableString *urlString = [NSMutableString stringWithString:EVENT_REQUEST_URL_STRING];
	[urlString appendString:@"?type=eventpost"];
	[urlString appendFormat:@"&locationlat=%f", [event.location.latitude doubleValue]];
	[urlString appendFormat:@"&locationlon=%f", [event.location.longitude doubleValue]];
	[urlString appendFormat:@"&eventname=%@", event.name];
	[urlString appendFormat:@"&starttime=%i000", (int)[event.startTime timeIntervalSince1970]];
	[urlString appendFormat:@"&endtime=%i000", (int)[event.endTime timeIntervalSince1970]];
	/** @todo Remove the substring limit on the device unique identifier */
	[urlString appendFormat:@"&userid=%@", [[[UIDevice currentDevice] uniqueIdentifier] substringFromIndex:24]];
	[urlString appendString:@"&resp=xml"];
	[urlString appendFormat:@"&desc=%@", event.details];

	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSLog(@"%@", escapedUrlString);
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];

	// Make the request to get the data
	NSLog(@"Submitting URL: %@", urlString);
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];

	NSLog(@"submitEvent returned data %@", [[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding] autorelease]);
	// Parse the request
	NSError *err = nil;
	DDXMLDocument *responseXml = [[DDXMLDocument alloc] initWithData:responseData options:0 error:&err];
	NSLog(@"response: %@", [responseXml stringValue]);
	if (err) {
		NSLog(@"Error loading response XML: %@", err);
	} else {
		// Get the event ID from the server's response
		NSArray *nodes = [responseXml nodesForXPath:@"./EventPostResponse/eventid" error:&err];
		if ([nodes count] > 0) {
			NSLog(@"Setting server ID to %@", [[nodes objectAtIndex:0] stringValue]);
			event.serverId = [[nodes objectAtIndex:0] stringValue];
			[[event managedObjectContext] save:&err];
		}
	}
	
}

@end
