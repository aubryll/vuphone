//
//  MapFilterViewController.m
//  Commencement
//
//  Created by Aaron Thompson on 10/10/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import "MapFilterViewController.h"

#define SECONDS_PER_DAY 60*60*24

@implementation MapFilterViewController

- (void)viewDidLoad
{
	dateFormatter = [[NSDateFormatter alloc] init];
	[dateFormatter setDateFormat:@"MMM d"];

	// Set the default filter parameters
	startDayNumber = -1;
	endDayNumber = -1;
	
	self.startDayNumber = 0;
	self.endDayNumber = 30;
	self.tags = [NSMutableArray array];

	[startDateSlider setValue:(float)startDayNumber];
	[endDateSlider setValue:(float)endDayNumber];
}

- (IBAction)dismissSheet:(id)sender
{
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction)startDateSliderValueChanged:(id)sender
{
	self.startDayNumber = (NSUInteger)((UISlider *)sender).value;
}

- (IBAction)endDateSliderValueChanged:(id)sender
{
	self.endDayNumber = (NSUInteger)((UISlider *)sender).value;
}

- (void)setStartDayNumber:(NSUInteger)dayNumber
{
	if (dayNumber != startDayNumber)
	{
		startDayNumber = dayNumber;

		if (self.startDayNumber > self.endDayNumber) {
			[endDateSlider setValue:(float)self.startDayNumber];
			self.endDayNumber = self.startDayNumber;
		}
		
		NSDate *date = [NSDate dateWithTimeIntervalSinceNow:self.startDayNumber*SECONDS_PER_DAY];
		[startDateLabel setText:[dateFormatter stringFromDate:date]];
		[self rebuildPredicate];
	}
}

- (void)setEndDayNumber:(NSUInteger)dayNumber
{
	if (dayNumber != endDayNumber)
	{
		endDayNumber = dayNumber;

		if (self.endDayNumber < self.startDayNumber) {
			startDateSlider.value = (float)self.endDayNumber;
			self.startDayNumber = self.endDayNumber;
		}
		
		NSDate *date = [NSDate dateWithTimeIntervalSinceNow:self.endDayNumber*SECONDS_PER_DAY];
		[endDateLabel setText:[dateFormatter stringFromDate:date]];
		[self rebuildPredicate];
	}
}

- (void)rebuildPredicate
{
	NSPredicate *startDatePredicate = [NSPredicate predicateWithFormat:
									   @"startTime >= %@", [NSDate dateWithTimeIntervalSinceNow:self.startDayNumber*SECONDS_PER_DAY]];
	NSPredicate *endDatePredicate = [NSPredicate predicateWithFormat:
									 @"endTime <= %@", [NSDate dateWithTimeIntervalSinceNow:(self.endDayNumber+1)*SECONDS_PER_DAY]];
	NSPredicate *tagsPredicate = [NSPredicate predicateWithFormat:@"TRUEPREDICATE"];
	
	if (filterPredicate) {
		[filterPredicate release];
	}
	filterPredicate = [[NSCompoundPredicate andPredicateWithSubpredicates:
						[NSArray arrayWithObjects:startDatePredicate, endDatePredicate, tagsPredicate, nil]] retain];

	[[NSNotificationCenter defaultCenter] postNotificationName:VU_MAPFILTER_CHANGED_NOTIFICATION object:filterPredicate];
}

- (NSPredicate *)predicate
{
	if (!filterPredicate) {
		[self rebuildPredicate];
	}
	return filterPredicate;
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}

@synthesize startDayNumber;
@synthesize endDayNumber;
@synthesize tags;
@synthesize filterPredicate;

@end
