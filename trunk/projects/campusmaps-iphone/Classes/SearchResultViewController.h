//
//  SearchResultViewController.h
//  CampusMaps
//
//  Created by Ben Wibking on 11/5/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface SearchResultViewController : UIViewController {

	IBOutlet UIView * searchResultsScrollView;
	IBOutlet UIPageControl * searchResultsPageControl;
	
	NSMutableArray * searchResultViewArray;
	
	IBOutlet UIView * searchResultView;
	IBOutlet UILabel * titleLabel;
	IBOutlet UILabel * subtitleLabel;
	IBOutlet UITextView * detailTextView;
}

@end
