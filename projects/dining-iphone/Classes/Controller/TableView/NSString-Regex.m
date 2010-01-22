//
//  NSString-Regex.m
//  Dining
//
//  Created by Aaron Thompson on 1/21/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "NSString-Regex.h"

@implementation NSString (Regex)


char *g_discardMediaTags[] = {
	"<img", "<iframe"
};
int g_discardMediaTagLengths[] = {
	4, 7
};
#define TOTAL_DISCARD_MEDIA_TAGS 2

- (NSString *)stripMedia
{
    NSString *result = nil;
    
    const char *charString = [self UTF8String];
    int len = strlen(charString);
    char buffer[len + 1];
    buffer[len + 1] = '\0';
    char *pBuffer = buffer;
    const char *start = charString;    
    const char *end = start + len;    
    
    BOOL done = NO;
    while (done == NO)
    {
        int i = 0;
        char *foundTag = NULL;
        int foundTagLength = 0;
        const char *minLoc = end;
        for (i = 0; i < TOTAL_DISCARD_MEDIA_TAGS; i++)
        {
            char *tag = g_discardMediaTags[i];
            char *loc = strcasestr(start, tag);
            if (loc != NULL)
            {
                if (loc < minLoc)
                {
                    minLoc = loc;
                    foundTag = tag;
                    foundTagLength = g_discardMediaTagLengths[i];
                }
            }
        }
		
        if (foundTag == NULL)
        {
            int left = end - start;
            strncpy(pBuffer, start, left);
            pBuffer += left;
            *pBuffer = '\0';    
            done = YES;
        }
        else
        {
            if (minLoc != NULL && minLoc != start)
            {
                int left = minLoc - start;
                strncpy(pBuffer, start, left);
                pBuffer += left;
                *pBuffer = '\0';
                start = minLoc;
            }    
			
            const char *endTag = NULL;
            for (endTag = start + foundTagLength + 1; endTag < end; endTag++)
            {
                if (*endTag == '>')
                {
                    start = endTag + 1;
                    break;
                }
            }
			
            if (endTag == NULL || endTag >= end)
            {
                done = YES;
            }
        }
    }
    
    result = [NSString stringWithUTF8String:buffer];
    
    return result;
}

@end
