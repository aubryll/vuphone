//
//  MapFilterViewController.h
//  Commencement
//
//  Created by Aaron Thompson on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

#define VU_MAPFILTER_CHANGED_NOTIFICATION @"mapFilterChangedNotification"

//#define FILTER_SHEET_HEIGHT 140.0
#define FILTER_SHEET_HEIGHT 70.0


@interface MapFilterViewController : UIViewController {
	IBOutlet UISlider *startDateSlider;
	IBOutlet UILabel *startDateLabel;
	IBOutlet UISlider *endDateSlider;
	IBOutlet UILabel *endDateLabel;
	IBOutlet UITextView *tagsTextView;
	NSDateFormatter *dateFormatter;
	
	NSUInteger startDayNumber;
	NSUInteger endDayNumber;
	NSMutableArray *tags;
	
	NSPredicate *filterPredicate;
}

- (IBAction)startDateSliderValueChanged:(id)sender;
- (IBAction)endDateSliderValueChanged:(id)sender;
- (void)rebuildPredicate;
- (NSPredicate *)predicate;

@property (assign, getter=setStartDayNumber) NSUInteger startDayNumber;
@property (assign, getter=setEndDayNumber)   NSUInteger endDayNumber;
@property (copy) NSArray *tags;
@property (retain) NSPredicate *filterPredicate;

@end
