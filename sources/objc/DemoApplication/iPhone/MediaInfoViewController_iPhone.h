//
//  MediaInfoViewController_iPhone.h
//  Borhan
//
//  Created by Pavel on 29.02.12.
//  Copyright (c) 2012 Borhan. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MediaPlayer/MediaPlayer.h>
#import <MessageUI/MessageUI.h>
#import <MessageUI/MFMailComposeViewController.h>
#import <BORHANPlayerSDK/KPViewController.h>

@class AppDelegate_iPhone;

extern const CGRect PlayerCGRect;

@interface MediaInfoViewController_iPhone : UIViewController <MFMailComposeViewControllerDelegate, UINavigationControllerDelegate> {
    
    AppDelegate_iPhone *app;
    
    BorhanMediaEntry *mediaEntry;
    
    IBOutlet UIScrollView *scrollMain;
    
    IBOutlet UIView *viewIntro;
    IBOutlet UIView *viewDescription;

    IBOutlet BorhanThumbView *imgThumb;
    IBOutlet UILabel *labelTitle;

    IBOutlet UILabel *labelVTitle;
    IBOutlet UILabel *labelVDuration;

    IBOutlet UILabel *textDescription;
    
    IBOutlet UIButton *buttonPlay;
    IBOutlet UIButton *buttonCategory;
    
    IBOutlet UIView *viewShare;
    
    NSString *categoryName;

    KPViewController* playerViewController;
        
}

- (IBAction)menuBarButtonPressed:(UIButton *)button;
- (IBAction)categoryBarButtonPressed:(UIButton *)button;
- (IBAction)playButtonPressed;
- (IBAction)shareButtonPressed:(UIButton *)button;

// Supporting PlayerSDK 
- (void)stopAndRemovePlayer;
- (void)toggleFullscreen:(NSNotification *)note;

@property (nonatomic, retain) BorhanMediaEntry *mediaEntry;
@property (nonatomic, retain) NSString *categoryName;

@end
