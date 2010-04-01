//
//  NSManagedObject-IsNew.h
//  Events
//
//  Created by Aaron Thompson on 9/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@interface NSManagedObject(IsNew)

/*
 @method isNew 
 @abstract   Returns YES if this managed object is new and has not yet been saved yet into the persistent store.
 */
- (BOOL)isNew;

@end
