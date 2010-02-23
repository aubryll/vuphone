//
//  MapViewController.m
//  Campus Tour
//
//  Created by Guy on 2/16/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "MapViewController.h"
#import "Waypoint.h"
#import "../KissXML/DDXMLDocument.h"


@implementation MapViewController

/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        // Custom initialization
    }
    return self;
}
*/


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	[self centerOnCampus:nil];
	NSData *iconData = [NSData dataWithContentsOfFile:[[[NSBundle mainBundle] resourcePath]
													   stringByAppendingPathComponent:ANNOTATION_IMAGE_FILE]];
	annotationImage = [[UIImage alloc] initWithData:iconData];
	
	NSData *responseData = [NSData dataWithContentsOfFile:[[[NSBundle mainBundle] resourcePath] 
												stringByAppendingPathComponent:POI_REQUEST_ALTERNATIVE]];
    // Parse the request
    //NSError *err = nil;
    //DDXMLDocument *responseXml = [[DDXMLDocument alloc] initWithData:responseData options:0 error:&err];
    //if (err) {
	//	NSLog(@"Error loading response XML: %@", err);
	//	[responseXml release];
	//}
	//else {
		//create waypoints from data in the xml files.
		//DDXMLNode *prop;
		
		//prop = (DDXMLNode *)[[node nodesForXPath:@"./ms:facilities/ms:description" error:&err] objectAtIndex:0];
		//NSString *test = [prop stringValue];
		//not sure how to get data out of the xml file...
	//}

	//create dummy wayPt
	//Waypoint *wayPt = [[Waypoint alloc] init];
	//wayPt.name = @"Center of Campus";
	//wayPt.description = @"Something bad happened.";
	//[mapView addAnnotation:wayPt];
	 NSXMLParser *parser = [[NSXMLParser alloc] initWithData:responseData];
	 [parser setDelegate:self];
	 [parser setShouldProcessNamespaces:NO];
	 [parser setShouldReportNamespacePrefixes:NO];
	 [parser setShouldResolveExternalEntities:NO];
	 [parser parse];
	 [parser release];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {

	
}

- (IBAction)centerOnCampus:(id)sender {
	CLLocationCoordinate2D location;
	location.latitude = 36.142;
	location.longitude = -86.8044;
	
	MKCoordinateRegion region;
	MKCoordinateSpan span;
	span.longitudeDelta = 0.01;
	span.latitudeDelta = 0.01;
	
	region.span = span;
	region.center = location;
	
	[mapView setRegion:region animated:TRUE];
	[mapView regionThatFits:region];
}


- (void)dealloc {
    [super dealloc];
}


- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	static NSString *reuseIdentifier = @"reusedAnnView";
	MKAnnotationView* annView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
	annView.image = annotationImage;
	annView.canShowCallout = YES;
	annView.rightCalloutAccessoryView = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
	
	return [annView autorelease];	
}

-(void)parser:(NSXMLParser *)parser didStartElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qualifiedName 
   attributes:(NSDictionary *)attributeDict 
{
	lookingFor = NOTHING;
	if ([namespaceURI compare:@""] == NSOrderedSame) {
		if ([elementName compare:@"waypoint"] == NSOrderedSame) {
			tmpWaypoint = [[Waypoint alloc] init];
		} else if ([elementName compare:@"coordinate"] == NSOrderedSame) {
			if (tmpWaypoint == NULL) {
				tmpWaypoint = [[Waypoint alloc] init];
			}
			double longitude = [[attributeDict valueForKey:@"longitude"] doubleValue];
			double latitude = [[attributeDict valueForKey:@"latitude"] doubleValue];
			[tmpWaypoint setLocation:longitude latitude:latitude];
			 } else if ([elementName compare:@"location"] == NSOrderedSame) {
			if (tmpWaypoint == NULL) {
				tmpWaypoint = [[Waypoint alloc] init];
			}
			tmpWaypoint.name = [attributeDict valueForKey:@"name"];
			tmpWaypoint.num = [[attributeDict valueForKey:@"num"] intValue];
		} else if ([elementName compare:@"audiopath"] == NSOrderedSame) {
			lookingFor = AUDIOPATH;
		} else if ([elementName compare:@"description"] == NSOrderedSame) {
			lookingFor = DESCRIPTION;
		} else {
		}
	}
}


- (void)parser:(NSXMLParser *)parser didEndElement:(NSString *)elementName namespaceURI:(NSString *)namespaceURI qualifiedName:(NSString *)qName
{
	if ([namespaceURI compare:@""] == NSOrderedSame) 
	{
		if ([elementName compare:@"waypoint"] == NSOrderedSame) {
			[mapView addAnnotation:tmpWaypoint];
		} else {
		}
	}
}	


- (void)parser:(NSXMLParser *)parser foundCharacters:(NSString *)string
{
	if (lookingFor == AUDIOPATH) {
		tmpWaypoint.audioFilePath = string;
	} else if (lookingFor == DESCRIPTION) {
		tmpWaypoint.description = string;
	} else {
		//do nothing
	}
	//reset the state
	lookingFor = NOTHING;
}



@end
