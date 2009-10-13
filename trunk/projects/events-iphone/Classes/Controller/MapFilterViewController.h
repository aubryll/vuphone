//
//  MapFilterViewController.h
//  Events
//
//  Created by Aaron Thompson on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#define FILTER_SHEET_HEIGHT 140.0

@interface MapFilterViewController : UIViewController {
	IBOutlet UISlider *startDateSlider;
	IBOutlet UILabel *startDateLabel;
	IBOutlet UISlider *endDateSlider;
	IBOutlet UILabel *endDateLabel;
	IBOutlet UITextView *tagsTextView;
	
	NSUInteger startDayNumber;
	NSUInteger endDayNumber;
	NSMutableArray *tags;
}

- (IBAction)startDateSliderValueChanged:(id)sender;
- (IBAction)endDateSliderValueChanged:(id)sender;

@property (assign) NSUInteger startDayNumber;
@property (assign) NSUInteger endDayNumber;
@property (copy) NSArray *tags;

@end
