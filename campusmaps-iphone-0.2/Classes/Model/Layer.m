// 
//  Layer.m
//  CampusMaps
//
//  Created by Ben Wibking on 10/16/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "Layer.h"
#import "NSManagedObjectContext-Convenience.h"

@implementation Layer 

+ (NSSet *)allLayers:(NSManagedObjectContext *)context
{
	NSSet *result = [context fetchObjectsForEntityName:ENTITY_NAME_LAYER
								   withPredicateString:@"TRUEPREDICATE"];

	return result;
}

@dynamic name;
@dynamic POIs;

@end
