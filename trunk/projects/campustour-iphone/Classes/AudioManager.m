//
//  AudioManager.m
//  Campus Tour
//
//  Created by Demetri Miller on 3/23/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "AudioManager.h"



@implementation AudioManager

static AudioManager *sharedAudioManager = nil;


#pragma mark -
#pragma mark Instance Methods

// Plays the specified audio file. [audioPlayer play] is asynchronous so
// no need to perform this task in a separate thread.
- (void)playAudioFile:(NSString *)filename ofType:(NSString *)format
{	
	NSString *soundPath = [[NSBundle mainBundle] pathForResource: filename
														  ofType: format];
	NSURL *soundURL = [[[NSURL alloc] initFileURLWithPath: soundPath] autorelease];
	
	// delete the existing audio player, if one exists. This will prevent
    // sounds from overlapping if you press lots of buttons fast.
    if (audioPlayer){
        [audioPlayer stop];
        [audioPlayer release];
        audioPlayer = nil;
    }
	
	audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL: soundURL 
														 error: nil];
	[audioPlayer play];
	
}

- (void)pausePlayback {
	[audioPlayer pause];
}

- (void)resumePlayback {
	[audioPlayer play];
}

- (void)stopAndReleasePlayer {
	[audioPlayer stop];
	[audioPlayer release];
	audioPlayer = nil;
}

- (BOOL)isAudioPlaying {
	return audioPlayer.playing;
}

- (NSString *)audioPlayerURL {
	return [audioPlayer.url absoluteString];
}


#pragma mark -
#pragma mark Singleton Methods

+ (id)sharedAudioManager
{
	if (sharedAudioManager == nil) {
		sharedAudioManager = [[super allocWithZone: NULL] init];
	}
	return sharedAudioManager;
}

+ (id)allocWithZone:(NSZone *)zone
{
	return [[self sharedAudioManager] retain];
}

- (id)copyWithZone:(NSZone *)zone
{
	return self;
}

- (id)retain
{
	return self;
}

- (NSUInteger)retainCount
{
	return NSUIntegerMax; // Denotes an object that cannot be released.
}

- (void)released
{
	// Do nothing.
}

- (id)autorelease
{
	return self;
}

@synthesize audioPlayer;

@end
