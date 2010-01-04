//
//  RemotePOILoader.h
//  POIs
//
//  Created by Aaron Thompson on 9/7/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DDXML.h"
#import "POI.h"

#define POI_REQUEST_URL_STRING @"http://www.vanderbilt.edu/map/gml/vu.gml"
#define POI_REQUEST_ALTERNATIVE @"buildings.xml"
@interface RemotePOILoader : NSObject {

}

+ (NSArray *)getPOIsFromServerIntoContext:(NSManagedObjectContext *)context;
+ (void)getDataFromXMLNode:(DDXMLNode *)node intoPOI:(POI *)POI;

@end
