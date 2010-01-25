// 
//  HourRange.m
//  Dining
//
//  Created by Aaron Thompson on 1/14/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import "HourRange.h"

#import "Restaurant.h"

@implementation HourRange 

@dynamic openMinute;
@dynamic closeMinute;
@dynamic day;
@dynamic order;
@dynamic restaurant;
@dynamic contiguousWith;
@dynamic contiguousWithBackwards;
@dynamic contiguousCloseMinute;

- (id)initWithEntity:(NSEntityDescription *)entity insertIntoManagedObjectContext:(NSManagedObjectContext *)context
{
	self = [super initWithEntity:entity insertIntoManagedObjectContext:context];
	if (self != nil) {
		_contiguousCloseMinute = -1;
	}
	return self;
}


- (NSNumber *)openHour {
	return [NSNumber numberWithInt:[self.openMinute intValue] / 60];
}

- (NSNumber *)closeHour {
	return [NSNumber numberWithInt:[self.closeMinute intValue] / 60];
}

- (NSString *)description {
	return [NSString stringWithFormat:@"%@: %@",
			[self.day capitalizedString],
			[self formattedHoursString]];
}

- (NSString *)formattedHoursString
{
	// Prefixed all with an 'f' to avoid namespace conflicts
	int fopenHour, fopenMinute, fcloseHour, fcloseMinute;
	
	if ([self.openMinute intValue] == 0 && [self.closeMinute intValue] == 1440) {
		// It's open 24-hours, so just add it and continue
		return @"24 hours";
	} else if (self.contiguousWith != nil) {
		fopenHour = [[self openHour] intValue];
		fopenMinute = [self.openMinute intValue];
		fcloseHour = [[self.contiguousWith closeHour] intValue];
		fcloseMinute = [self.contiguousWith.closeMinute intValue];
	} else {
		fopenHour = [[self openHour] intValue];
		fopenMinute = [self.openMinute intValue];
		fcloseHour = [[self closeHour] intValue];
		fcloseMinute = [self.closeMinute intValue];
	}

	return [NSString stringWithFormat:@"%i:%.2i - %i:%.2i",
			fopenHour,
			fopenMinute % 60,
			fcloseHour,
			fcloseMinute % 60];
}

- (NSInteger)contiguousCloseMinute
{
	if (_contiguousCloseMinute < 0)
	{
		// Loop through all related HourRanges in the contiguousWith chain, avoiding cycles
		HourRange *curRange = self;
		do {
			_contiguousCloseMinute += [curRange.closeMinute integerValue];
		} while (curRange.contiguousWith != self && (curRange = curRange.contiguousWith));
	}
	
	return _contiguousCloseMinute;
}

@end
