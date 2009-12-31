//
//  This class was created by Nonnus,
//  who graciously decided to share it with the CocoaHTTPServer community.
//

#import <Foundation/Foundation.h>
#import "HTTPConnection.h"


@interface SensorMockHTTPConnection : HTTPConnection
{
	int dataStartIndex;
	NSMutableArray* multipartData;
	BOOL postHeaderOK;
}

- (BOOL)isBrowseable:(NSString *)path;
- (NSString *)createBrowseableIndexForRequestedPath:(NSString *)url withFilePath:(NSString *)path;

- (BOOL)supportsPOST:(NSString *)path withSize:(UInt64)contentLength;

@end