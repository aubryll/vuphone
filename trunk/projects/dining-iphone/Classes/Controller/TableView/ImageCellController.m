//
//  ImageCellController.m
//  Dining
//
//  Created by Aaron Thompson on 1/31/10.
//  Copyright 2010 Vanderbilt University. All rights reserved.
//

#import "ImageCellController.h"
#import "ImageViewCell.h"

@implementation ImageCellController

@synthesize image;

#pragma mark VUTableViewCellController

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
	return nil;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *imageIdentifier = @"imageID";
	
	ImageViewCell *cell = (ImageViewCell *)[tableView dequeueReusableCellWithIdentifier:imageIdentifier];
	if (cell == nil) {
		cell = [[[ImageViewCell alloc] initWithStyle:UITableViewCellStyleDefault
									 reuseIdentifier:imageIdentifier] autorelease];
	}

	[cell setImage:self.image];
	
	return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
	ImageViewCell *cell = (ImageViewCell *)[self tableView:tableView cellForRowAtIndexPath:indexPath];
	
	return [cell height];
}

@end
