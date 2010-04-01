//
//  NSManagedObject-IsNew.m
//  Commencement
//
//  Created by Aaron Thompson on 9/11/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "NSManagedObject-IsNew.h"


@implementation NSManagedObject(IsNew)

- (BOOL)isNew 
{
    NSDictionary *vals = [self committedValuesForKeys:nil];
    return [vals count] == 0;
}

@end