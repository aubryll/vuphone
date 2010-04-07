//
//  CourseXMLReader.h
//  Campus Tour
//
//  Created by sma1 on 3/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface CourseXMLReader : NSObject {

}

+ (NSMutableArray *)coursesFromXMLAtPath:(NSString *)path;

@end
