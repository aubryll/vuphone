//
//  SensorMockViewController.m
//  SensorMock
//
//  Created by Ben Gotow on 9/21/09.
//  Copyright __MyCompanyName__ 2009. All rights reserved.
//

#import "SensorMockViewController.h"
#import "SensorMock.h"
#import "SensorMockViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>
#import "SensorMockHTTPConnection.h"

@implementation SensorMockViewController

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    // Step 1: Create the location manager
    test = [[CLLocationManager alloc] init];
    [test setDelegate: self];
    [test beginRecording];
    [test startUpdatingLocation];
    [test startUpdatingHeading];
    [test setDesiredAccuracy: kCLLocationAccuracyBest];
    [test setDistanceFilter: kCLDistanceFilterNone];
    [test setHeadingFilter: kCLHeadingFilterNone];
    
 
    // Step 2: start up the web server to serve the files in the documents folder
    httpServer = [HTTPServer new];
    [httpServer setType:@"_http._tcp."];
    [httpServer setConnectionClass:[SensorMockHTTPConnection class]];
    [httpServer setPort: 8080];
    [httpServer setDocumentRoot:[NSURL fileURLWithPath: [[NSString stringWithFormat:@"~/Documents"] stringByExpandingTildeInPath]]];
    
    NSError *error;
    if(![httpServer start:&error])
        NSLog(@"Error starting HTTP Server: %@", error);
        
    [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(go) userInfo:nil repeats:NO];
}

- (void)go
{
    // Start recording video
    NSArray * types = [UIImagePickerController availableMediaTypesForSourceType: UIImagePickerControllerSourceTypeCamera];
    UIImagePickerController * c = [[UIImagePickerController alloc] init];
    if ([types containsObject: (NSString*)kUTTypeMovie])
        [c setMediaTypes: [NSArray arrayWithObject: (NSString*)kUTTypeMovie]];
    else{
        [c setSourceType: UIImagePickerControllerSourceTypePhotoLibrary];
        [c setMediaTypes: [NSArray arrayWithObject: (NSString*)kUTTypeImage]];
    }
    [c setAllowsImageEditing: NO];
    [c setDelegate: self];
    [self presentModalViewController:c animated:NO];
}

- (void)dealloc 
{
    [test dealloc];
    [super dealloc];
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    NSDateFormatter * f = [[[NSDateFormatter alloc] init] autorelease];
    [f setDateFormat: @"yyyy-MM-dd-HH-mm-ss"];
    
    NSString * timestamp = [f stringFromDate: [NSDate date]];
    NSString * videoPath = [[NSString stringWithFormat:@"~/Documents/%@.mov", timestamp] stringByExpandingTildeInPath];
    NSString * recordingPath = [[NSString stringWithFormat:@"~/Documents/%@.rec", timestamp] stringByExpandingTildeInPath];
            
    if ([[info objectForKey: UIImagePickerControllerMediaType] isEqualToString: (NSString*)kUTTypeMovie]) {
        NSURL * videoURL = [info objectForKey: UIImagePickerControllerMediaURL];
        if (videoURL) {
            // move the video file to the shared folder
            [[NSFileManager defaultManager] copyItemAtPath:[videoURL absoluteString] toPath:videoPath error:nil];
        }
    }
    
    [test saveRecordingToFile: recordingPath];
    [self dismissModalViewControllerAnimated: NO];
}

#pragma mark CLLocationManagerDelegate Implementation

- (void)locationManager:(CLLocationManager *)m didUpdateHeading:(CLHeading *)newHeading
{
    [text setText: [NSString stringWithFormat:@"%d",[[test getRecording] count]]];
    NSLog(@"Got heading: %@", [newHeading description]);
}

- (void)locationManager:(CLLocationManager *)m didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    [text setText: [NSString stringWithFormat:@"%d",[[test getRecording] count]]];
    NSLog(@"Got location: %@", [newLocation description]);
}

@end
