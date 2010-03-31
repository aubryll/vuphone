//
//  XMLReaderUtilities.h
//  Campus Tour
//
//  Created by sma1 on 3/30/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DDXML.h"
#import "DDXMLNode.h"

@interface XMLReaderUtilities : NSObject {

}

+ (NSString *)getXMLData:(DDXMLNode *)node tag:(NSString *)tagName attribute:(NSString *)attr;

@end
