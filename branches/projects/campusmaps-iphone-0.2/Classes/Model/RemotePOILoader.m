//
//  RemotePOILoader.m
//  POIs
//
//  Created by Aaron Thompson on 9/7/09.
//
/* Sample XML server response:
 <POIrequestresponse>
	 <POI>
		 <name>Test</name>
		 <loc>
			<lat>36.1437</lat>
			<lon>-86.8046</lon>
		 </loc>
		 <owner>true</owner>
		 <start>1248290654565</start>
		 <end>1248291254565</end>
		 <POIid>1</POIid>
		 <lastupdate>1248291254565</lastupdate>
	 </POI>
 </POIrequestresponse>
 
 */

#define SAMPLE_POI_REQUEST_RESPONSE 0

#import "RemotePOILoader.h"
#import "Layer.h"

@implementation RemotePOILoader

+ (NSArray *)getPOIsFromServerIntoContext:(NSManagedObjectContext *)context
{
	// Format the url string
	NSMutableString *urlString = [NSMutableString stringWithString:POI_REQUEST_URL_STRING];
	NSLog(@"Requesting URL: %@", urlString);
	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];
	
	// Make the request to get the data
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];

	// If URL request failed. Use the static XML file.
	if (responseData == nil || [responseData length] == 0) {
		NSLog(@"Failed to retrieve building data from server.\nUsing static XML file.");
		responseData = [NSData dataWithContentsOfFile:[[[NSBundle mainBundle] resourcePath] 
													   stringByAppendingPathComponent:POI_REQUEST_ALTERNATIVE]];
	}
	
	// Parse the request
	NSError *err = nil;
	DDXMLDocument *responseXml = [[DDXMLDocument alloc] initWithData:responseData options:0 error:&err];
	if (err) {
		NSLog(@"Error loading response XML: %@", err);
		[responseXml release];
		return nil;
	}
	
	NSArray *nodes = [responseXml nodesForXPath:@"./wfs:FeatureCollection/gml:featureMember" error:&err];
	NSMutableArray *POIs = [[NSMutableArray alloc] initWithCapacity:[nodes count]];
	if ([nodes count] > 0)
	{
		// Fetch any layer for now
		Layer *defaultLayer = [[Layer allLayers:context] anyObject];

		for (DDXMLNode *node in nodes)
		{
			NSString *serverId = [(DDXMLNode *)[[node nodesForXPath:@"./ms:facilities/ms:FACILITY_NUMBER" error:&err] objectAtIndex:0] stringValue];
			
			POI *poi = [POI POIWithServerId:serverId inContext:context];

			if (!poi) {
				// Create a new POI
				poi = [NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_POI
													inManagedObjectContext:context];
				poi.layer = defaultLayer;
			}

			[RemotePOILoader getDataFromXMLNode:node intoPOI:poi];
			
			[context save:&err];
			if (err) {
				NSLog(@"Error saving POI: %@", err);
				// Get rid of this POI
				[context rollback];
			} else {
				[POIs addObject:poi];
			}
		}		
	}
	else
	{
		NSLog(@"No nodes found: %@", responseXml);
	}
	
	[responseXml release];
	
	NSLog(@"Finished loading POIs from server");

	return [POIs autorelease];
}

+ (void)getDataFromXMLNode:(DDXMLNode *)node intoPOI:(POI *)poi
{
	DDXMLNode *prop;
	NSError *err;

	prop = (DDXMLNode *)[[node nodesForXPath:@"./ms:facilities/ms:FACILITY_NUMBER" error:&err] objectAtIndex:0];
	poi.serverId = [prop stringValue];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./ms:facilities/ms:FACILITY_NAME" error:&err] objectAtIndex:0];
	poi.name = [prop stringValue];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./ms:facilities/ms:FACILITY_URL" error:&err] objectAtIndex:0];
	// The URL string is always all caps, but the real URL isn't really
	poi.url = [[prop stringValue] lowercaseString];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./ms:facilities/ms:FACILITY_REMARKS" error:&err] objectAtIndex:0];
	poi.details = [prop stringValue];

	prop = (DDXMLNode *)[[node nodesForXPath:@"./ms:facilities/gml:boundedBy/gml:Box/gml:coordinates" error:&err] objectAtIndex:0];
	NSArray *coordinates = [[prop stringValue] componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@", "]];
	if ([coordinates count] == 4) {
		double lat1 = [[coordinates objectAtIndex:0] doubleValue];
		double lon1 = [[coordinates objectAtIndex:1] doubleValue];
		double lat2 = [[coordinates objectAtIndex:2] doubleValue];
		double lon2 = [[coordinates objectAtIndex:3] doubleValue];
		
		double weirdLat = (lat1 + lat2) / 2.0;
		double weirdLon = (lon1 + lon2) / 2.0;

		[poi setEPSG900913CoordinatesLat:weirdLat andLon:weirdLon];
	}
}

@end
