//
//  MapViewController.h
//  Campus Tour
//
//  Created by Guy on 2/16/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import <Waypoint.h>

#define ANNOTATION_IMAGE_FILE @"annotation-v.png"
#define POI_REQUEST_ALTERNATIVE @"waypoints-db.xml"

enum XMLSTATE {
	NOTHING,
	DESCRIPTION,
	AUDIOPATH
};

@interface MapViewController : UIViewController <MKMapViewDelegate> {

	IBOutlet MKMapView* mapView;
	UIImage* annotationImage;
	Waypoint *tmpWaypoint;
	enum XMLSTATE lookingFor;
}

- (IBAction)centerOnCampus:(id)sender;
//temporary parser code.
- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName;
-(void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qualifiedName  attributes:(NSDictionary *)attributeDict;
- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string;
@end
