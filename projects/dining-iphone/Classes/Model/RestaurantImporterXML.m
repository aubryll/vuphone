//
//  RestaurantImporterXML.m
//  restaurants
//
//  Created by Aaron Thompson on 01/10/10
//

#import "RestaurantImporterXML.h"

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

@implementation RestaurantImporterXML

+ (void)getRestaurantsFromXMLAtPath:(NSString *)path intoContext:(NSManagedObjectContext *)context
{
	NSData *xmlData = [NSData dataWithContentsOfFile:path];

	// Parse the request
	NSError *err = nil;
	DDXMLDocument *restaurantXml = [[DDXMLDocument alloc] initWithData:xmlData options:0 error:&err];
	if (err) {
		NSLog(@"Error loading %@: %@", path, err);
		[restaurantXml release];
		return;
	}
	
	// Find the first response
	NSArray *nodes = [restaurantXml nodesForXPath:@"/restaurants/restaurant" error:&err];
	for (DDXMLNode *node in nodes)
	{
		// Fetch the restaurant from our DB.  If it doesn't exist, create a new one.
		NSString *name = [(DDXMLNode *)[[node nodesForXPath:@"./name" error:&err] objectAtIndex:0] stringValue];

		Restaurant *restaurant = [Restaurant restaurantWithName:name inContext:context];
		if (!restaurant) {
			restaurant = [NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_RESTAURANT
													   inManagedObjectContext:context];
		}

		// Clear out all old HourRanges
		[restaurant deleteAllOpenHours];

		// Load in the data from the XML file
		[RestaurantImporterXML getDataFromXMLNode:node intoRestaurant:restaurant];

		
		[context save:&err];
		if (err) {
			NSLog(@"Error saving restaurant: %@", err);
			NSArray* detailedErrors = [[err userInfo] objectForKey:NSDetailedErrorsKey];
			if([detailedErrors count] > 0)
			{
				for(NSError* detailedError in detailedErrors) {
					NSLog(@"  DetailedError: %@", [detailedError userInfo]);
				}
			}
			// Get rid of this restaurant
			[context rollback];
		}
	}
	
	[restaurantXml release];
}

+ (void)getDataFromXMLNode:(DDXMLNode *)node intoRestaurant:(Restaurant *)restaurant
{
	NSError *err;

	restaurant.name = [[node nodeForXPath:@"./name" error:&err] stringValue];
	restaurant.details = [[node nodeForXPath:@"./description" error:&err] stringValue];
	restaurant.type = [[node nodeForXPath:@"./type" error:&err] stringValue];
	restaurant.phone = [[node nodeForXPath:@"./phone" error:&err] stringValue];

	double lat = [[[node nodeForXPath:@"./location/latitude" error:&err] stringValue] doubleValue];
	restaurant.latitude = [NSNumber numberWithDouble:lat];
	double lon = [[[node nodeForXPath:@"./location/longitude" error:&err] stringValue] doubleValue];
	restaurant.longitude = [NSNumber numberWithDouble:lon];

	restaurant.urlString = [[node nodeForXPath:@"./url" error:&err] stringValue];
	restaurant.imageUrlString = [[node nodeForXPath:@"./image" error:&err] stringValue];

	BOOL temp = [[[node nodeForXPath:@"./offCampus" error:&err] stringValue] boolValue];
	restaurant.offCampus = [NSNumber numberWithBool:temp];

	temp = [[[node nodeForXPath:@"./acceptsMealPlan" error:&err] stringValue] boolValue];
	restaurant.acceptsMealPlan = [NSNumber numberWithBool:temp];
	
	temp = [[[node nodeForXPath:@"./acceptsMealMoney" error:&err] stringValue] boolValue];
	restaurant.acceptsMealMoney = [NSNumber numberWithBool:temp];
	
	// Import hours
	for (DDXMLNode *rangeNode in [node nodesForXPath:@"./hours/range" error:&err]) {
		HourRange *range = [NSEntityDescription insertNewObjectForEntityForName:ENTITY_NAME_HOUR_RANGE
														 inManagedObjectContext:[restaurant managedObjectContext]];

		range.day = [[[rangeNode nodeForXPath:@"./day" error:&err] stringValue] capitalizedString];

		NSArray *openComponents = [[[rangeNode nodeForXPath:@"./open" error:&err] stringValue] componentsSeparatedByString:@":"];
		int hour = [[openComponents objectAtIndex:0] intValue];
		int minute = [[openComponents objectAtIndex:1] intValue];
		range.openMinute = [NSNumber numberWithInt:hour*60 + minute];

		NSArray *closeComponents = [[[rangeNode nodeForXPath:@"./close" error:&err] stringValue] componentsSeparatedByString:@":"];
		hour = [[closeComponents objectAtIndex:0] intValue];
		minute = [[closeComponents objectAtIndex:1] intValue];
		range.closeMinute = [NSNumber numberWithInt:hour*60 + minute];
		
		[restaurant addOpenHoursObject:range];
	}
}


@end
