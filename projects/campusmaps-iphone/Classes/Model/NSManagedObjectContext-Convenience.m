//
//  NSManagedObjectContext-Convenience.m
//  Events
//
//  Created by Aaron Thompson on 12/1/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "NSManagedObjectContext-Convenience.h"


@implementation NSManagedObjectContext(Convenience)

- (NSSet *)fetchAllObjectsForEntityName:(NSString *)entityName
{
	return [self fetchObjectsForEntityName:entityName withPredicateString:@"TRUEPREDICATE"];
}

// Convenience method to fetch the array of objects for a given Entity
// name in the context, optionally limiting by a predicate or by a predicate
// made from a format NSString and variable arguments.
//
- (NSSet *)fetchObjectsForEntityName:(NSString *)entityName
				 withPredicateString:(NSString *)predicateString, ...
{
	NSPredicate *predicate;
	va_list variadicArguments;
	va_start(variadicArguments, predicateString);
	predicate = [NSPredicate predicateWithFormat:predicateString
									   arguments:variadicArguments];
	va_end(variadicArguments);
	
	return [self fetchObjectsForEntityName:entityName withPredicate:predicate];
}

- (NSSet *)fetchObjectsForEntityName:(NSString *)entityName
					   withPredicate:(NSPredicate *)predicate
{
	NSEntityDescription *entity = [NSEntityDescription entityForName:entityName inManagedObjectContext:self];
	
	NSFetchRequest *request = [[NSFetchRequest alloc] init];
	[request setEntity:entity];

	[request setPredicate:predicate];
	
	NSError *error = nil;
	NSArray *results = [self executeFetchRequest:request error:&error];
	if (error != nil)
	{
		[NSException raise:NSGenericException format:[error description]];
	}
	
	[request release];
	return [NSSet setWithArray:results];
}

@end
