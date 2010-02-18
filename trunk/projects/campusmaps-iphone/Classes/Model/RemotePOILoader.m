//
//  RemotePOILoader.m
//  POIs
//
//  Created by Aaron Thompson on 9/7/09.
//

#define SAMPLE_POI_REQUEST_RESPONSE 0

#import "RemotePOILoader.h"
#import "Layer.h"

@implementation RemotePOILoader

+ (NSArray *)getPOIsFromServerIntoContext:(NSManagedObjectContext *)context
{
	// Override this and use the static data for now
/*
	// Format the url string
	NSMutableString *urlString = [NSMutableString stringWithString:POI_REQUEST_URL_STRING];
	NSLog(@"Requesting URL: %@", urlString);
	NSString *escapedUrlString = [urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
	NSURL *searchUrl = [NSURL URLWithString:escapedUrlString];
	
	// Make the request to get the data
	NSData *responseData = [NSData dataWithContentsOfURL:searchUrl];
*/
	NSData *responseData = nil;

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
	
	NSArray *nodes = [responseXml nodesForXPath:@"./buildings/feature" error:&err];
	NSMutableArray *POIs = [[NSMutableArray alloc] initWithCapacity:[nodes count]];
	if ([nodes count] > 0)
	{
		// Fetch any layer for now
		Layer *defaultLayer = [[Layer allLayers:context] anyObject];

		for (DDXMLNode *node in nodes)
		{
			NSString *name = [(DDXMLNode *)[[node nodesForXPath:@"./facility_name" error:&err] objectAtIndex:0] stringValue];
			
			POI *poi = [POI POIWithName:name inContext:context];

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

//	prop = (DDXMLNode *)[[node nodesForXPath:@"./feature/ms:FACILITY_NUMBER" error:&err] objectAtIndex:0];
//	poi.serverId = [prop stringValue];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./facility_name" error:&err] objectAtIndex:0];
	poi.name = [[prop stringValue] capitalizedString];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./search_keywords" error:&err] objectAtIndex:0];
	poi.searchKeywords = [[prop stringValue] capitalizedString];
	
	prop = (DDXMLNode *)[[node nodesForXPath:@"./FACILITY_URL" error:&err] objectAtIndex:0];
	// The URL string is always all caps, but the real URL isn't really
	poi.url = [[prop stringValue] lowercaseString];
	prop = (DDXMLNode *)[[node nodesForXPath:@"./FACILITY_REMARKS" error:&err] objectAtIndex:0];
	poi.details = [prop stringValue];

	prop = (DDXMLNode *)[[node nodesForXPath:@"./coordinates" error:&err] objectAtIndex:0];
	NSArray *coordinates = [[prop stringValue] componentsSeparatedByCharactersInSet:[NSCharacterSet characterSetWithCharactersInString:@", "]];
	if ([coordinates count]/* == 4*/) {
		// Average the coordinates, which are (I'm guessing) given as the corners of the building
		double latSum = 0.0;
		double lonSum = 0.0;
		for (int i = 0; i+1 < [coordinates count]; i += 2) {
			latSum += [[coordinates objectAtIndex:i] doubleValue];
			lonSum += [[coordinates objectAtIndex:i+1] doubleValue];
		}
		
		double weirdLat = latSum / [coordinates count];
		double weirdLon = lonSum / [coordinates count];

		[poi setEPSG900913CoordinatesLat:weirdLat andLon:weirdLon];
	}
}

@end
