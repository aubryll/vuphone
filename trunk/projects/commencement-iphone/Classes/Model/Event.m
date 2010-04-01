// 
//  Event.m
//  Commencement
//
//  Created by Aaron Thompson on 9/9/09.
//  Copyright 2009 Vanderbilt University. All rights reserved.
//

#import "Event.h"
#import "Location.h"
#import "EntityConstants.h"
#import "NSManagedObjectContext-Convenience.h"

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
			VUCommencementourceOfficialCalendar,
			@"Commons",
			@"Athletics",
			@"Facebook",
			VUCommencementourceUser,
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


- (BOOL)validateServerId:(id *)value error:(NSError **)error
{
	// Allow a nil server ID
	if (*value == nil) {
		return YES;
	}
	
	// Don't allow empty strings
	if ([*value length] == 0) {
		if (error != NULL)
			*error = [[[NSError alloc] initWithDomain:EVENT_ERROR_DOMAIN
												 code:SERVER_ID_INVALID_CODE
											 userInfo:nil] autorelease];
		return NO;
	}
	
	// Don't allow duplicate server IDs
	if ([Event duplicateServerId:self.serverId existsInContext:[self managedObjectContext]])
	{
		if (error != NULL)
			*error = [[[NSError alloc] initWithDomain:EVENT_ERROR_DOMAIN
												 code:SERVER_ID_EXISTS_CODE
											 userInfo:nil] autorelease];
		return NO;
	}
	
	// This is a unique server ID
	return YES;
}

+ (BOOL)duplicateServerId:(NSString *)anId existsInContext:(NSManagedObjectContext *)context {
	NSSet *duplicates = [context
						 fetchObjectsForEntityName:VUEntityNameEvent
						 withPredicateString:@"serverId = %@", anId];

	return [duplicates count] > 1;
}

+ (Event *)eventWithServerId:(NSString *)anId inContext:(NSManagedObjectContext *)context {
	NSSet *Commencement = [context
						 fetchObjectsForEntityName:VUEntityNameEvent
						 withPredicateString:@"serverId = %@", anId];
	
	return [Commencement anyObject];
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
