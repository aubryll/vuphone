#import <UIKit/UIKit.h>

@interface VUTableViewController : UITableViewController
{
	NSArray *tableGroups;
}

- (NSArray *)tableGroups;
- (void)clearTableGroups;
- (void)updateAndReload;

@end
