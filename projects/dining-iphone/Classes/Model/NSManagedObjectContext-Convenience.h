//
//  NSManagedObjectContext-Convenience.h
//  Events
//
//  Created by Aaron Thompson on 12/1/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface NSManagedObjectContext(Convenience)

// Convenience method to fetch the array of objects for a given Entity
// name in the context, optionally limiting by a predicate
//
- (NSArray *)fetchObjectsForEntityName:(NSString *)entityName
						 withPredicate:(NSPredicate *)predicate;

// Convenience method to fetch the set of objects for a given Entity
// name in the context, optionally limiting by a predicate
// made from a format NSString and variable arguments.
//
- (NSSet *)fetchObjectsForEntityName:(NSString *)newEntityName
				 withPredicateString:(NSString *)predicateString, ...;

@end
