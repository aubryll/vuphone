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
@dynamic restaurant;

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

- (NSString *)formattedHoursString {
	return [NSString stringWithFormat:@"%i:%.2i - %i:%.2i",
			[[self openHour] intValue],
			[self.openMinute intValue] % 60,
			[[self closeHour] intValue],
			[self.closeMinute intValue] % 60];
}

@end
