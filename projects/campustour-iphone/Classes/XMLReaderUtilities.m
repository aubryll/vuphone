//
//  XMLReaderUtilities.m
//  Campus Tour
//
//  Created by sma1 on 3/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "XMLReaderUtilities.h"


@implementation XMLReaderUtilities


//currently doesn't support xpath returning multiple nodes, but could be reworked to add index as an additional argument
+(NSString *)getXMLData:(DDXMLNode *)node tag:(NSString *)tagName attribute:(NSString*)attr
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
