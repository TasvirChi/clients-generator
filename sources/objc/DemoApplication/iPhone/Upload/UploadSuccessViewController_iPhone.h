//
//  UploadSuccessViewController_iPhone.h
//  Borhan
//
//  Created by Pavel on 06.03.12.
//  Copyright (c) 2012 Borhan. All rights reserved.
//

#import <UIKit/UIKit.h>

@class AppDelegate_iPhone;

@interface UploadSuccessViewController_iPhone : UIViewController {
    
    AppDelegate_iPhone *app;
    
    
    IBOutlet UILabel *labelTitle;
    IBOutlet UIView *viewMain;
    
    IBOutlet UILabel *labelThankYou;
    
    IBOutlet UILabel *labelText;
    
}

@end
