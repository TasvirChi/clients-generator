//
//  UploadProcessViewController_iPhone.h
//  Borhan
//
//  Created by Pavel on 06.03.12.
//  Copyright (c) 2012 Borhan. All rights reserved.
//

#import <UIKit/UIKit.h>

@class AppDelegate_iPhone;

@interface UploadProcessViewController_iPhone : UIViewController {
    
    AppDelegate_iPhone *app;
    
    
    IBOutlet UILabel *labelTitle;
    IBOutlet UIView *viewMain;
    
    IBOutlet UILabel *labelUploading;
    IBOutlet UIProgressView *progressView;
    
    NSDictionary *data;
    
    IBOutlet UIActivityIndicatorView *activityView;
    
    IBOutlet UIButton *buttonMenu;
    
    
    UIAlertView *cancelAlert;
}

@property (nonatomic, retain) NSDictionary *data;
@property (nonatomic, retain) UIAlertView *cancelAlert;

@end
