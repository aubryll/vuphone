// 
//  Event.m
//  Events
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "Event.h"
#import "Location.h"
#import "EntityConstants.h"

@implementation Event 

@dynamic ownerDeviceId;
@dynamic source;
@dynamic url;
@dynamic name;
@dynamic startTime;
@dynamic details;
@dynamic endTime;
@dynamic location;
@dynamic serverId;
@dynamic ratings;

+ (NSArray *)allSources {
	return [NSArray arrayWithObjects:
			@"Official Calendar",
			@"Commons",
			@"Athletics",
			@"Facebook",
			VUEventSourceUser,
			nil];
}

- (NSString *)startDateString {
	NSDateFormatter	*dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
	[dateFormatter setDateFormat:@"eeee, MMMM d"];
	
	if (self.startTime) {
		return [dateFormatter stringFromDate:self.startTime];
	} else {
		// There is some sort of error, so just return today's date
		return [dateFormatter stringFromDate:[NSDate date]];
	}
}

- (BOOL)isEditableByDeviceWithId:(NSString *)deviceId {
	return [self.ownerDeviceId isEqualToString:deviceId];
}

#pragma mark MKAnnotation methods

- (NSString *)title {
	return self.name;
}

- (NSString *)subtitle {
	return self.location.name;
}

- (CLLocationCoordinate2D)coordinate {
	return [self.location coordinate];
}
	
- (void)dealloc
{
	[super dealloc];
}

@end