//
//  Layer.h
//  CampusMaps
//
//  Created by Ben Wibking on 10/16/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <CoreData/CoreData.h>


@interface Layer :  NSManagedObject  
{
}

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSSet* POIs;

@end


@interface Layer (CoreDataGeneratedAccessors)
- (void)addPOIsObject:(NSManagedObject *)value;
- (void)removePOIsObject:(NSManagedObject *)value;
- (void)addPOIs:(NSSet *)value;
- (void)removePOIs:(NSSet *)value;

@end

