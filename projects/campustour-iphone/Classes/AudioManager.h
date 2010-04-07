//
//  AudioManager.h
//  Campus Tour
//
//  Created by Demetri Miller on 3/23/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
#import <AudioToolbox/AudioToolbox.h>
#import <MediaPlayer/MPVolumeView.h>
#import <UIKit/UIKit.h>

/*
 *	Singleton class which handles all audio playback during
 *	the tour.
 */
@interface AudioManager : NSObject <AVAudioPlayerDelegate> {
	
	AVAudioPlayer		* audioPlayer;
}

@property (nonatomic, retain) AVAudioPlayer *audioPlayer;

+ (id)sharedAudioManager;
- (void)playAudioFile:(NSString *)filename ofType:(NSString *)format;
- (void)pausePlayback;
- (void)resumePlayback;
- (void)stopAndReleasePlayer;
- (BOOL)isAudioPlaying;
- (NSString *)audioPlayerURL;

@end
