//
//  RemoteEventLoader.m
//  Commencement
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

#define SAMPLE_EVENT_REQUEST_RESPONSE 0

#import "RemoteEventLoader.h"
#import "EntityConstants.h"

@interface RemoteEventLoader (PrivateMethods)

+ (void)doSubmitEvent:(Event *)event;

@end


@implementation RemoteEventLoader

+ (NSArray *)getCommencementEventsFromServerSince:(NSDate *)updated intoContext:(NSManagedObjectContext *)context
{
	// Format the url string
	NSMutableString *urlString = [NSMutableString stringWithString:EVENT_REQUEST_URL_STRING];
	[urlString appendString:@"?type=eventrequest"];
	[urlString appendFormat:@"&lat=%f", CAMPUS_CENTER_LATITUDE];
	[urlString appendFormat:@"&lon=%f", CAMPUS_CENTER_LONGITUDE];
	[urlString appendFormat:@"&updatetime=%i", (updated == nil) ? 0 : (int)[updated timeIntervalSince1970]];
	[urlString appendFormat:@"&dist=%i", 100000];	// distance is measured in meters
	[urlString appendFormat:@"&userid=%@", [[UIDevice currentDevice] uniqueIdentifier]];
	[urlString appendString:@"&tags=commencement"];
	[urlString appendString:@"&resp=xml"];
	NSLog(@"Requesting URL: %@", urlString);
	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];
	// Make the request to get the data
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];
	
	// Parse the request
	NSError *err = nil;
	DDXMLDocument *responseXml = [[DDXMLDocument alloc] initWithData:responseData options:0 error:&err];
	if (err) {
		NSLog(@"Error loading response XML: %@", err);
		[responseXml release];
		return nil;
	}
	
	// Find the first response
	NSArray *nodes = [responseXml nodesForXPath:@"./EventRequestResponse/Event" error:&err];
	NSMutableArray *events = [[NSMutableArray alloc] initWithCapacity:[nodes count]];
	if ([nodes count] > 0)
	{
		for (DDXMLNode *node in nodes)
		{
			// Fetch the location from our DB.  If it doesn't exist, create a new one.
			NSString *locationServerId = [(DDXMLNode *)[[node nodesForXPath:@"./Loc/Id" error:&err] objectAtIndex:0] stringValue];
			Location *location = [Location locationWithServerId:locationServerId inContext:context];
			if (!location) {
				location = [NSEntityDescription insertNewObjectForEntityForName:VUEntityNameLocation inManagedObjectContext:context];
			}
			
			[RemoteEventLoader getDataFromXMLNode:node intoLocation:location];
			
			
			// Fetch the event from our DB.  If it doesn't exist, create a new one.
			NSString *serverId = [(DDXMLNode *)[[node nodesForXPath:@"./EventId" error:&err] objectAtIndex:0] stringValue];
			Event *event = [Event eventWithServerId:serverId inContext:context];
			if (!event) {
				// Create a new event
				event = [NSEntityDescription insertNewObjectForEntityForName:VUEntityNameEvent inManagedObjectContext:context];
				event.location = location;
			}
			
			[RemoteEventLoader getDataFromXMLNode:node intoEvent:event];
			
			[context save:&err];
			if (err) {
				NSLog(@"Error saving event: %@", err);
				// Get rid of this event
				[context rollback];
			} else {
				[events addObject:event];
			}
		}
	}
	else
	{
		// No Events were found
		#if TARGET_IPHONE_SIMULATOR
			NSLog(@"No nodes found: %@", responseXml);
		#endif
	}
	
	[responseXml release];
	
	return [events autorelease];
	
}


+ (NSArray *)getEventsFromServerBetween:(NSDate *)startDate and:(NSDate *)endDate 
						   updatedSince:(NSDate *)updated intoContext:(NSManagedObjectContext *)context
{
#if SAMPLE_EVENT_REQUEST_RESPONSE
	NSData *responseData = [NSData dataWithContentsOfFile:@"/Users/thompsonaaron/PROGRAMMING/VUPhone/trunk/projects/Commencement-iphone/sampleEventRequestResponse.xml"];
#else
	// Format the url string
	NSMutableString *urlString = [NSMutableString stringWithString:EVENT_REQUEST_URL_STRING];
	[urlString appendString:@"?type=eventrequest"];
	[urlString appendFormat:@"&lat=%f", CAMPUS_CENTER_LATITUDE];
	[urlString appendFormat:@"&lon=%f", CAMPUS_CENTER_LONGITUDE];
	[urlString appendFormat:@"&updatetime=%i", (updated == nil) ? 0 : (int)[updated timeIntervalSince1970]];
	[urlString appendFormat:@"&endtime=%i", (endDate == nil) ? 0: (int)[endDate timeIntervalSince1970]];
	[urlString appendFormat:@"&starttime=%i", (startDate == nil) ? 0: (int)[startDate timeIntervalSince1970]];
//	[urlString appendString:@"&updatetime=0"];
	[urlString appendFormat:@"&dist=%i", 100000];	// distance is measured in meters
	[urlString appendFormat:@"&userid=%@", [[UIDevice currentDevice] uniqueIdentifier]];
	[urlString appendString:@"&resp=xml"];
	NSLog(@"Requesting URL: %@", urlString);
	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];
	// Make the request to get the data
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];
#endif

	// Parse the request
	NSError *err = nil;
	DDXMLDocument *responseXml = [[DDXMLDocument alloc] initWithData:responseData options:0 error:&err];
	if (err) {
		NSLog(@"Error loading response XML: %@", err);
		[responseXml release];
		return nil;
	}
	
	// Find the first response
	NSArray *nodes = [responseXml nodesForXPath:@"./EventRequestResponse/Event" error:&err];
	NSMutableArray *Commencement = [[NSMutableArray alloc] initWithCapacity:[nodes count]];
	if ([nodes count] > 0)
	{
		for (DDXMLNode *node in nodes)
		{
			// Fetch the location from our DB.  If it doesn't exist, create a new one.
			NSString *locationServerId = [(DDXMLNode *)[[node nodesForXPath:@"./Loc/Id" error:&err] objectAtIndex:0] stringValue];
			Location *location = [Location locationWithServerId:locationServerId inContext:context];
			if (!location) {
				location = [NSEntityDescription insertNewObjectForEntityForName:VUEntityNameLocation inManagedObjectContext:context];
			}

			[RemoteEventLoader getDataFromXMLNode:node intoLocation:location];


			// Fetch the event from our DB.  If it doesn't exist, create a new one.
			NSString *serverId = [(DDXMLNode *)[[node nodesForXPath:@"./EventId" error:&err] objectAtIndex:0] stringValue];
			Event *event = [Event eventWithServerId:serverId inContext:context];
			if (!event) {
				// Create a new event
				event = [NSEntityDescription insertNewObjectForEntityForName:VUEntityNameEvent inManagedObjectContext:context];
				event.location = location;
			}

			[RemoteEventLoader getDataFromXMLNode:node intoEvent:event];

			
			[context save:&err];
			if (err) {
				NSLog(@"Error saving event: %@", err);
				// Get rid of this event
				[context rollback];
			} else {
				[Commencement addObject:event];
			}
		}
	}
	else
	{
		// No Commencement were found
//		NSLog(@"No nodes found: %@", responseXml);
	}
	
	[responseXml release];

	return [Commencement autorelease];
}

+ (void)getDataFromXMLNode:(DDXMLNode *)node intoEvent:(Event *)event
{
	DDXMLNode *prop;
	NSError *err;

	prop = (DDXMLNode *)[[node nodesForXPath:@"./Name" error:&err] objectAtIndex:0];
	event.name = [prop stringValue];
	event.name = [event.name stringByReplacingOccurrencesOfString:@"Commencement Event - " withString:@""];
	event.name = [event.name stringByReplacingOccurrencesOfString:@"Commencement Events," withString:@""];
	event.name = [event.name stringByReplacingOccurrencesOfString:@"Commencement Event -" withString:@""];
	event.name = [event.name stringByReplacingOccurrencesOfString:@"Commencement Event," withString:@""];

	NSArray *nodes = [node nodesForXPath:@"./Description" error:&err];
	if ([nodes count] > 0) {
		prop = (DDXMLNode *)[nodes objectAtIndex:0];
		event.details = [prop stringValue];
	}

	prop = (DDXMLNode *)[[node nodesForXPath:@"./Start" error:&err] objectAtIndex:0];
	event.startTime = [NSDate dateWithTimeIntervalSince1970:[[prop stringValue] intValue]];

	prop = (DDXMLNode *)[[node nodesForXPath:@"./End" error:&err] objectAtIndex:0];
	event.endTime = [NSDate dateWithTimeIntervalSince1970:[[prop stringValue] intValue]];

	prop = (DDXMLNode *)[[node nodesForXPath:@"./EventId" error:&err] objectAtIndex:0];
	event.serverId = [prop stringValue];

	// Hard-coding the source for now
	event.source = VUCommencementourceOfficialCalendar;
}

+ (void)getDataFromXMLNode:(DDXMLNode *)node intoLocation:(Location *)location
{
	DDXMLNode *prop;
	NSError *err;
	NSArray *nodes;

	prop = (DDXMLNode *)[[node nodesForXPath:@"./Loc/Id" error:&err] objectAtIndex:0];
	location.serverId = [prop stringValue];
	
	// Name
	nodes = [node nodesForXPath:@"./Loc/Name" error:&err];
	if ([nodes count] > 0) {
		prop = (DDXMLNode *)[nodes objectAtIndex:0];
		location.name = [prop stringValue];
	}

	// Lat/lon
	prop = (DDXMLNode *)[[node nodesForXPath:@"./Loc/Lat" error:&err] objectAtIndex:0];
	location.latitude = [NSDecimalNumber decimalNumberWithString:[prop stringValue]];

	prop = (DDXMLNode *)[[node nodesForXPath:@"./Loc/Lon" error:&err] objectAtIndex:0];
	location.longitude = [NSDecimalNumber decimalNumberWithString:[prop stringValue]];
}

+ (void)submitEvent:(Event *)event
{
	[NSThread detachNewThreadSelector:@selector(doSubmitEvent:) toTarget:self withObject:event];
}

+ (void)doSubmitEvent:(Event *)event
{
	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	
	NSMutableString *urlString = [NSMutableString stringWithString:EVENT_REQUEST_URL_STRING];
	[urlString appendString:@"?type=eventpost"];
	[urlString appendFormat:@"&locationlat=%f", [event.location.latitude doubleValue]];
	[urlString appendFormat:@"&locationlon=%f", [event.location.longitude doubleValue]];
	[urlString appendFormat:@"&eventname=%@", event.name];
	[urlString appendFormat:@"&starttime=%i", (int)[event.startTime timeIntervalSince1970]];
	[urlString appendFormat:@"&endtime=%i", (int)[event.endTime timeIntervalSince1970]];
	/** @todo Remove the substring limit on the device unique identifier */
	[urlString appendFormat:@"&userid=%@", [[[UIDevice currentDevice] uniqueIdentifier] substringFromIndex:24]];
	[urlString appendString:@"&resp=xml"];
	[urlString appendFormat:@"&desc=%@", event.details];

	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];

	// Make the request to get the data
//	NSLog(@"Submitting URL: %@", urlString);
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];

//	NSLog(@"submitEvent returned data %@", [[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding] autorelease]);
	// Parse the request
	NSError *err = nil;
	DDXMLDocument *responseXml = [[DDXMLDocument alloc] initWithData:responseData options:0 error:&err];
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
	
	[pool release];
}

@end
